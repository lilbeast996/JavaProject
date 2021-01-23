package java_project_management_app_cp.database_access;

import java_project_management_app_cp.ProjectExceptions;
import java_project_management_app_cp.services.ServicesAccounts;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DatabasesVisualization {
    private Connection connection;
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;
    private Statement statement;
    private ResultSet resultSet;

    public DatabasesVisualization(Connection connection, DataInputStream inputStream, DataOutputStream outputStream) {
        this.connection = connection;
        this.outputStream = outputStream;
        this.inputStream = inputStream;
    }

    public void getAllNames(){
        String query = null;
        List<String> names = new ArrayList<>();
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            query = ("SELECT * FROM sales_representative");
            resultSet = statement.executeQuery(query);
            resultSet.beforeFirst();
            while(resultSet.next()) {
                String singleName = resultSet.getString("Username");
                names.add(singleName);
            }
            String[] possibleNames = names.toArray(new String[names.size()]);
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(possibleNames);
        } catch (SQLException exception) {
            ProjectExceptions.writeToFile(exception);
        }catch (IOException exception){
            ProjectExceptions.writeToFile(exception);
        }finally {
            closingConnections();
        }

    }

    public void createDataSetForChart(){
        DefaultCategoryDataset dataset = null;
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String query = ("SELECT * FROM analysis");
            dataset = new DefaultCategoryDataset();
            resultSet = statement.executeQuery(query);
            resultSet.beforeFirst();
            while (resultSet.next()) {
                dataset.addValue(Integer.parseInt(resultSet.getString("SaledQuantity")), resultSet.getString("Username"), "2021");
            }
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(dataset);
        }catch (SQLException exception){
            ProjectExceptions.writeToFile(exception);
        }catch (IOException exception){
            ProjectExceptions.writeToFile(exception);
        }finally {
            closingConnections();
        }
    }

    public void getAllProducts(){
        String query = null;
        List<String> products = new ArrayList<>();

        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            query = ("SELECT * FROM products");
            resultSet = statement.executeQuery(query);
            resultSet.beforeFirst();
            while(resultSet.next()) {
                String row = resultSet.getString("ID") + "   | " + resultSet.getString("Brand") + "   | "
                        + resultSet.getString("Model") + "   | " + resultSet.getString("Price");
                products.add(row);
            }
            String[] possibleProducts = products.toArray(new String[products.size()]);
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(possibleProducts);
        } catch (SQLException exception) {
            ProjectExceptions.writeToFile(exception);
        } catch (IOException exception) {
            ProjectExceptions.writeToFile(exception);
        }finally {
            closingConnections();
        }
    }

    public void getAllPurchases(){
        String query = null;
        HashSet<String> purchase = new HashSet<>();
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            query = ("SELECT * FROM purchase where Username = '" + ServicesAccounts.getUsername() + "'");
            resultSet = statement.executeQuery(query);
            resultSet.beforeFirst();
            while(resultSet.next()) {
                String row = resultSet.getString("IDPurchase");
                purchase.add(row);
            }
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(purchase);
        } catch (SQLException exception) {
            ProjectExceptions.writeToFile(exception);
        } catch (IOException exception) {
            ProjectExceptions.writeToFile(exception);
        }finally {
            closingConnections();
        }
    }

    public void getAllClients() {
        String query = null;
        String row = null;

        List<String> clients = new ArrayList<>();
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            query = ("SELECT * FROM clients where Username ='" + ServicesAccounts.getUsername() + "'");
            resultSet = statement.executeQuery(query);
            resultSet.beforeFirst();
            while (resultSet.next()) {
                row = resultSet.getString("Name");
                clients.add(row);
            }
            String[] possibleNames = clients.toArray(new String[clients.size()]);
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(possibleNames);
        } catch (SQLException exception) {
            ProjectExceptions.writeToFile(exception);
        } catch (IOException exception) {
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
