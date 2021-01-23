package java_project_management_app_cp.services;

import java_project_management_app_cp.ProjectExceptions;
import java_project_management_app_cp.models.Client;
import java_project_management_app_cp.models.Purchase;
import java_project_management_app_cp.database_access.DatabasesSalesRepresentative;
import java_project_management_app_cp.database_access.DatabasesVisualization;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.*;

import static java_project_management_app_cp.ProjectExceptions.writeToFile;

public class ServicesSalesRepresentative implements SalesRepresentative {
    private String query;
    private Connection connection;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String indicator;


    public ServicesSalesRepresentative(){}

    public ServicesSalesRepresentative(DataInputStream inputStream, DataOutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public void add() {
        connection = connect();
        Client client = new Client();
        DatabasesSalesRepresentative repositorySr = new DatabasesSalesRepresentative(connection, outputStream, inputStream);
        try {
            client.setName(inputStream.readUTF());
            client.setPhoneNumber(inputStream.readUTF());
        } catch (IOException exception) {
            ProjectExceptions.writeToFile(exception);
        }
        repositorySr.add(client);

    }

    @Override
    public void delete() {
        connection = connect();
        Client client = new Client();
        DatabasesSalesRepresentative repositorySr = new DatabasesSalesRepresentative(connection, outputStream, inputStream);
        try {
            client.setName(inputStream.readUTF());
            client.setPhoneNumber(inputStream.readUTF());
        } catch (IOException exception) {
            ProjectExceptions.writeToFile(exception);
        }
        repositorySr.delete(client);
    }

    @Override
    public void update() {
        connection = connect();
        Client client = new Client();
        DatabasesSalesRepresentative repositorySr = new DatabasesSalesRepresentative(connection, outputStream, inputStream);
        try {
            client.setName(inputStream.readUTF());
            client.setPhoneNumber(inputStream.readUTF());
        } catch (IOException exception) {
            ProjectExceptions.writeToFile(exception);
        }
        repositorySr.update(client);
    }

    @Override
    public void refresh() {
        connection = connect();
        this.query = "select * from clients where Username = '" + ServicesAccounts.getUsername() + "'";
        DatabasesSalesRepresentative repositorySr = new DatabasesSalesRepresentative(connection, outputStream, inputStream);
        repositorySr.refresh(query);
    }

    @Override
    public void visualize() {
        connection = connect();
        DatabasesVisualization repositoryVisualize = new DatabasesVisualization(connection, inputStream, outputStream);
        try {
            indicator = inputStream.readUTF();
        } catch (IOException exception) {
            ProjectExceptions.writeToFile(exception);
        }
        switch (indicator){
            case"getAllProducts": repositoryVisualize.getAllProducts(); break;
            case"getAllClients": repositoryVisualize.getAllClients(); break;
            case"getAllPurchases": repositoryVisualize.getAllPurchases(); break;
        }
    }

    @Override
    public void catalog() {
        connection = connect();
        DatabasesSalesRepresentative repositorySr = new DatabasesSalesRepresentative(connection, outputStream, inputStream);
        Purchase purchase = new Purchase();
        try {
            indicator = inputStream.readUTF();
        } catch (IOException exception) {
            ProjectExceptions.writeToFile(exception);
        }
        switch (indicator){
            case"changeQuantityToSpecificProduct":
                int newQuantity = 0;
                String modelToChange = null;
                try {
                    newQuantity = inputStream.readInt();
                    modelToChange = inputStream.readUTF();
                }catch (IOException exception){
                    ProjectExceptions.writeToFile(exception);
                }
                repositorySr.changeQuantityProduct(newQuantity, modelToChange); break;
            case"addPurchase":
                try{
                    purchase.setIDPurchase(inputStream.readUTF());
                    purchase.setBrand(inputStream.readUTF());
                    purchase.setModel(inputStream.readUTF());
                    purchase.setNameClient(inputStream.readUTF());
                    purchase.setQuantity(inputStream.readInt());
                }catch (IOException exception){
                    ProjectExceptions.writeToFile(exception);
                }
                repositorySr.pressButtonCatalogAdd(purchase); break;
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