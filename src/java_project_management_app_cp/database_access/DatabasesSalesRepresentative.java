package java_project_management_app_cp.database_access;

import java_project_management_app_cp.MailSending;
import java_project_management_app_cp.ProjectExceptions;
import java_project_management_app_cp.models.Purchase;
import java_project_management_app_cp.services.ServicesAccounts;
import java_project_management_app_cp.models.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.Vector;

import static java_project_management_app_cp.ProjectExceptions.writeToFile;

public class DatabasesSalesRepresentative {
    private String query;
    private Connection connection;
    private ResultSet resultSet;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private static DataOutputStream dos;
    private static DataInputStream dis;

    public DatabasesSalesRepresentative( DataOutputStream dos, DataInputStream dis) {
        this.dos = dos;
        this.dis = dis;
    }


    public void add(Client client){
        connection = connect();
        this.query = "insert into clients ( Name, PhoneNumber, Username) values( ?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getPhoneNumber());
            preparedStatement.setString(3, ServicesAccounts.getUsername());
            preparedStatement.executeUpdate();
            refresh("select * from clients where Username = '" + ServicesAccounts.getUsername() + "'");
        } catch (SQLException exception) {
            ProjectExceptions.writeToFile(exception);
        }finally {
            closingConnections();
        }
    }

    public synchronized void update(Client client){
        connection = connect();
        this.query = "update  clients set Name = ? where PhoneNumber = ? ;";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getPhoneNumber());
            preparedStatement.executeUpdate();
            refresh("select * from clients where Username = '" + ServicesAccounts.getUsername() + "'");
        } catch (SQLException  exception) {
            ProjectExceptions.writeToFile(exception);
        }finally {
            closingConnections();
        }
    }

    public void delete(Client client){
        connection = connect();
        this.query = "delete from clients where PhoneNumber = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, client.getPhoneNumber());
            preparedStatement.executeUpdate();
            refresh("select * from clients where Username = '" + ServicesAccounts.getUsername() + "'");
        } catch (SQLException  exception) {
            ProjectExceptions.writeToFile(exception);
        }finally {
            closingConnections();
        }
    }

    public void refresh(String query){
        connection = connect();
        String executeQuery = query;
        try {
            preparedStatement = connection.prepareStatement(executeQuery);
            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();

            int numberOfColumns = metaData.getColumnCount();
            Vector columnNames = new Vector();
            for (int column = 0; column < numberOfColumns; column++) {
                columnNames.addElement(metaData.getColumnLabel(column + 1));
            }

            Vector rows = new Vector();
            while (resultSet.next()) {
                Vector newRow = new Vector();

                for (int i = 1; i <= numberOfColumns; i++) {
                    newRow.addElement(resultSet.getObject(i));
                }

                rows.addElement(newRow);
            }
            ObjectOutputStream objectOutputStreams = new ObjectOutputStream(dos);
            objectOutputStreams.writeObject(rows);
            objectOutputStreams.writeObject(columnNames);
        } catch (SQLException  exception) {
            ProjectExceptions.writeToFile(exception);
        }catch (IOException exception){
            ProjectExceptions.writeToFile(exception);
        }finally {
            closingConnections();
        }
    }

    public synchronized void changeQuantityProduct(int newQuantity, String model)  {
        connection = connect();
        int quantityOld = 0;
        this.query = ("SELECT * FROM products WHERE Model = '" + model + "'");
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(query);
            resultSet.beforeFirst();
            while(resultSet.next()) {
                quantityOld = Integer.parseInt(resultSet.getString("Quantity"));
            }
            if(quantityOld<=10){
                new MailSending().sendMail("firstmailproject@gmail.com", model);
            }
            if( newQuantity > quantityOld){
                dos.writeBoolean(false);
            }else{
                String queryProductChange = "update products set Quantity = Quantity - " + String.valueOf(newQuantity)+ " where Model = '" + model + "'";
                Statement statementNew = null;
                try {
                    statementNew = connection.createStatement();
                    statementNew.executeUpdate(queryProductChange);
                } catch (SQLException e) {
                    ProjectExceptions.writeToFile(e);
                }
                dos.writeBoolean(true);
            }
        } catch (SQLException exception) {
            ProjectExceptions.writeToFile(exception);
        }catch (IOException exception){
            ProjectExceptions.writeToFile(exception);
        }finally {
            closingConnections();
        }
    }

    public void deletePurchase(String purchase){
        connection = connect();
        this.query = "delete from purchase where IDPurchase = ?";
        System.out.println(this.query);
        try{
            connection = connect();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, purchase);
            preparedStatement.executeUpdate();
            refresh("select IDPurchase, Brand, Model, Username, TimeOfBuying, SaledQuantity, NameClient from purchase where Username = '" +  ServicesAccounts.getUsername() + "'");
        } catch (SQLException exception) {
            ProjectExceptions.writeToFile(exception);
        }finally {
            closingConnections();

        }

    }

    public void pressButtonCatalogAdd(Purchase purchase) {
        connection = connect();
        this.query = "insert into purchase (IDPurchase, Brand, Model, Username, TimeOfBuying, SaledQuantity, NameClient) values(?, ?, ?, ?, ?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(2, purchase.getBrand());
            preparedStatement.setString(3, purchase.getModel());
            preparedStatement.setString(4, ServicesAccounts.getUsername());
            preparedStatement.setString(5, String.valueOf(LocalDate.now()));
            preparedStatement.setString(6, String.valueOf(purchase.getQuantity()));
            preparedStatement.setString(7, purchase.getNameClient());
            preparedStatement.setString(1, purchase.getIDPurchase());
            preparedStatement.executeUpdate();
            refresh("select IDPurchase, Brand, Model, Username, TimeOfBuying, SaledQuantity, NameClient from purchase where Username = '" +  ServicesAccounts.getUsername() + "'");
        }catch (SQLException exception){
            ProjectExceptions.writeToFile(exception);

        }finally {
            closingConnections();
        }
        this.query = "update  analysis set SaledQuantity = SaledQuantity + ?  where Username = ? ";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(purchase.getQuantity()));
            preparedStatement.setString(2, ServicesAccounts.getUsername());
            preparedStatement.executeUpdate();
        }catch (SQLException exception){
            ProjectExceptions.writeToFile(exception);
        }finally {
            closingConnections();
        }
    }

    private void closingConnections(){
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException exception) { ProjectExceptions.writeToFile(exception);}
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException exception) { ProjectExceptions.writeToFile(exception);}
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException exception) { ProjectExceptions.writeToFile(exception);}
        }
    }

    private  Connection connect () {
        try {
            return DriverManager.getConnection("jdbc:mysql://127.0.0.1/db_sap_solution", "root", "");
        } catch (SQLException sqlException) {
            writeToFile(sqlException);
        }
        return null;
    }
}
