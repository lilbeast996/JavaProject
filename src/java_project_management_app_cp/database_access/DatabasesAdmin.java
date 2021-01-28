package java_project_management_app_cp.database_access;

import java_project_management_app_cp.ProjectExceptions;
import java_project_management_app_cp.models.Product;
import java_project_management_app_cp.models.SalesRep;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.Random;
import java.util.Vector;

import static java_project_management_app_cp.ProjectExceptions.writeToFile;

public class DatabasesAdmin {
    private String  query;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private static DataOutputStream outputStream;
    private static DataInputStream inputStream;

    public DatabasesAdmin(DataOutputStream outputStream, DataInputStream inputStream) {
        this.outputStream = outputStream;
        this.inputStream = inputStream;
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
            ObjectOutputStream ois = new ObjectOutputStream(outputStream);
            ois.writeObject(rows);
            ois.writeObject(columnNames);

        } catch (SQLException exception) {
            ProjectExceptions.writeToFile(exception);
        }catch ( IOException exception){
            ProjectExceptions.writeToFile(exception);
        } finally{
            closingConnections();
        }
    }

    public void addProduct(Product product){
        connection = connect();
        this.query = "insert into products ( Brand, Model, Price, Quantity) values (  ?, ?, ?, ?);";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, product.getBrand());
            preparedStatement.setString(2, product.getModel());
            preparedStatement.setString(3, product.getPrice());
            preparedStatement.setString(4, product.getQuantity());
            preparedStatement.executeUpdate();
            refresh( this.query = "select  Brand, Model, Price, Quantity from products");
        } catch (SQLException exception) {
            ProjectExceptions.writeToFile(exception);
        }finally {
            closingConnections();
        }
    }

    public void deleteProduct(Product product){
        connection = connect();
        this.query = "delete from products where Model = ?";
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, product.getModel());
            preparedStatement.executeUpdate();
            refresh(this.query = "select  Brand, Model, Price, Quantity from products");
        } catch (SQLException exception) {
            ProjectExceptions.writeToFile(exception);
        }finally {
            closingConnections();
        }
    }

    public synchronized void updateProduct(Product product){
        connection = connect();
        this.query = "update  products set Brand =  ?, Price = ? , Quantity = ? where Model = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, product.getBrand());
            preparedStatement.setString(2, product.getPrice());
            preparedStatement.setString(3, product.getQuantity());
            preparedStatement.setString(4, product.getModel());
            preparedStatement.executeUpdate();
            refresh(this.query = "select  Brand, Model, Price, Quantity from products");
        } catch (SQLException exception) {
            ProjectExceptions.writeToFile(exception);
        }finally {
            closingConnections();
        }
    }

    public void addSr(SalesRep salesRep) {
        connection = connect();
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String generatedPassword = new String(array, Charset.forName("UTF-8"));
        this.query = "insert into sales_representative ( Name, PhoneNumber, NameFirm, Username, DefaultPassword) values( ?, ?, ?, ?, ?);";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, salesRep.getName());
            preparedStatement.setString(2, salesRep.getPhoneNumber());
            preparedStatement.setString(3, salesRep.getNameFirm());
            preparedStatement.setString(4, salesRep.getUsername());
            preparedStatement.setString(5, generatedPassword);
            preparedStatement.executeUpdate();
            refresh("select  Name, PhoneNumber, NameFirm, Username from sales_representative;");
            this.query = "insert into analysis ( Username, SaledQuantity) values( ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,salesRep.getUsername());
            preparedStatement.setString(2, "0");
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            ProjectExceptions.writeToFile(exception);
        }finally {
            closingConnections();
        }
    }

    public void deleteSr(SalesRep salesRep){
        connection = connect();
        this.query = "delete from sales_representative where Username = ?;";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, salesRep.getUsername());
            preparedStatement.executeUpdate();
            refresh("select  Name, PhoneNumber, NameFirm, Username from sales_representative;");

        } catch (SQLException exception) {
            ProjectExceptions.writeToFile(exception);
        }finally {
            closingConnections();
        }
    }

    public synchronized void updateSr(SalesRep salesRep ){
        connection = connect();
        this.query = "update  sales_representative set Name = ?, PhoneNumber = ?, NameFirm = ? where Username = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, salesRep.getName());
            preparedStatement.setString(2, salesRep.getPhoneNumber());
            preparedStatement.setString(3, salesRep.getNameFirm());
            preparedStatement.setString(4, salesRep.getUsername());
            preparedStatement.executeUpdate();
            refresh("select  Name, PhoneNumber, NameFirm, Username from sales_representative;");
        } catch (SQLException exception) {
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
