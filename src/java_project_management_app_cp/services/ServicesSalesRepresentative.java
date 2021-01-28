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

public class ServicesSalesRepresentative implements User {
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
        Client client = new Client();
        DatabasesSalesRepresentative repositorySr = new DatabasesSalesRepresentative(outputStream, inputStream);
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
        Client client = new Client();
        DatabasesSalesRepresentative repositorySr = new DatabasesSalesRepresentative( outputStream, inputStream);
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
        Client client = new Client();
        DatabasesSalesRepresentative repositorySr = new DatabasesSalesRepresentative( outputStream, inputStream);
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
        this.query = "select * from clients where Username = '" + ServicesAccounts.getUsername() + "'";
        DatabasesSalesRepresentative repositorySr = new DatabasesSalesRepresentative( outputStream, inputStream);
        repositorySr.refresh(query);
    }

    @Override
    public void visualize() {
        DatabasesVisualization repositoryVisualize = new DatabasesVisualization(inputStream, outputStream);
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


    public void catalog() {
        DatabasesSalesRepresentative repositorySr = new DatabasesSalesRepresentative( outputStream, inputStream);
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
                    modelToChange = inputStream.readUTF();
                    newQuantity = inputStream.readInt();
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
            case "showSpecificPurchase":
                try {
                    this.query = "select IDPurchase, Brand, Model, Username, TimeOfBuying, SaledQuantity, NameClient from purchase where Username = '" + ServicesAccounts.getUsername()
                            + "' AND IDPurchase = " + inputStream.readUTF();
                } catch (IOException exception) {
                    writeToFile(exception);
                }
                repositorySr.refresh(this.query); break;
            case "showAll":
                this.query = "select IDPurchase, Brand, Model, Username, TimeOfBuying, SaledQuantity, NameClient from purchase where Username = '" + ServicesAccounts.getUsername() + "'";
                repositorySr.refresh(this.query); break;
            case "deleteSpecificPurchase":
                String purchaseID = null;
                try{
                    purchaseID = inputStream.readUTF();
                }catch (IOException exception){
                    ProjectExceptions.writeToFile(exception);
                }
                repositorySr.deletePurchase(purchaseID);
                break;
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