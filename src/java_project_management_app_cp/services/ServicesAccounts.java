package java_project_management_app_cp.services;

import java_project_management_app_cp.ProjectExceptions;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.Base64;

import static java_project_management_app_cp.ProjectExceptions.writeToFile;

public class ServicesAccounts {
    private Connection connection;
    private Statement statement;;
    private ResultSet resultSet;
    private static String username;
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;
    private static String userType;

    public ServicesAccounts(DataInputStream inputStream, DataOutputStream outputStream){
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public boolean authenticateUser(String inputUser, String inputPass) {
        String query = null;
        connection = connect();
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            query = ("SELECT * FROM sales_representative WHERE Username='" + inputUser +  "'");
            resultSet = statement.executeQuery(query);
            if (!resultSet.next()){
                query = ("SELECT * FROM admin WHERE Username='" + inputUser +  "'");
                resultSet = statement.executeQuery(query);
                ServicesAdmin admin = new ServicesAdmin(inputStream, outputStream);
                UserType.setUserType(admin);
                UserType.setUserAdmin(admin);
                userType = "admin";
            }else{
                ServicesSalesRepresentative salesRepresentative = new ServicesSalesRepresentative(inputStream, outputStream);
                UserType.setUserType(salesRepresentative);
                UserType.setUserSalesRep(salesRepresentative);
                userType = "sr";
            }
            resultSet.beforeFirst();
            while(resultSet.next()) {
                String salt = resultSet.getString("Salt");
                String calculatedHash = getEncryptedPassword(inputPass, salt);
                if((calculatedHash.equals(resultSet.getString("Password"))) && (inputUser.equalsIgnoreCase(resultSet.getString("Username")))){
                    return true;
                }else{
                    return false;
                }
            }
        } catch (SQLException ex) {
            writeToFile(ex);
        }finally {
            closingConnections();
        }
        return false;
    }

    public String getEncryptedPassword(String password, String salt) {
        String algorithm = "PBKDF2WithHmacSHA1";
        int derivedKeyLength = 160;
        int iterations = 20000;
        if(salt == null)
        {
            return null;
        }
        byte[] saltBytes = Base64.getDecoder().decode(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, iterations, derivedKeyLength);
        SecretKeyFactory f = null;
        try {
            f = SecretKeyFactory.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            writeToFile(e);
        }
        byte[] encBytes = new byte[0];
        try {
            encBytes = f.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(encBytes);
    }

    public String getNewSalt() {
        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            writeToFile(e);
        }
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public boolean login() {
        String inputUser = null;
        String inputPass = null;
        try {
            inputUser = inputStream.readUTF();
            inputPass = inputStream.readUTF();
        } catch (IOException exception) {
            writeToFile(exception);
        }

        boolean status = false;
        status = authenticateUser(inputUser, inputPass);
        if (status) {
            setUsername(inputUser);
            try {
                outputStream.writeUTF(userType);
                status = true;
            } catch (IOException exception) {
               writeToFile(exception);
            }
        } else {
            try {
                outputStream.writeUTF("unknownUser");
            } catch (IOException exception) {
                writeToFile(exception);
            }
                status = false;
        }
        return status;
    }

    private  Connection connect () {
        try {
            return DriverManager.getConnection("jdbc:mysql://127.0.0.1/db_sap_solution", "root", "");
        } catch (SQLException sqlException) {
            writeToFile(sqlException);
        }
        return null;
    }

    public static void setUsername(String username) {
        ServicesAccounts.username = username;
    }

    public static String getUsername(){
        return ServicesAccounts.username;
    }

    public static String getUserType() {
        return userType;
    }


    private void closingConnections(){
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException exception) { ProjectExceptions.writeToFile(exception);}
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException exception) { ProjectExceptions.writeToFile(exception);}
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException exception) { ProjectExceptions.writeToFile(exception);}
        }
    }


}
