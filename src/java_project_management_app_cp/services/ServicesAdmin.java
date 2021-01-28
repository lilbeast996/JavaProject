package java_project_management_app_cp.services;


import java_project_management_app_cp.ProjectExceptions;
import java_project_management_app_cp.models.Product;
import java_project_management_app_cp.models.SalesRep;
import java_project_management_app_cp.database_access.DatabasesAdmin;
import java_project_management_app_cp.database_access.DatabasesVisualization;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.*;

import static java_project_management_app_cp.ProjectExceptions.writeToFile;

public class ServicesAdmin implements User {
    private String query;
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;
    private String indicator;

    public ServicesAdmin(){}

    public ServicesAdmin(DataInputStream inputStream, DataOutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public void add() {
        DatabasesAdmin databasesAdmin = new DatabasesAdmin(outputStream, inputStream);
        try {
            indicator = inputStream.readUTF();

            if (indicator.equalsIgnoreCase("product")) {
                Product product = new Product();
                product.setBrand(inputStream.readUTF());
                product.setModel(inputStream.readUTF());
                product.setPrice(inputStream.readUTF());
                product.setQuantity(inputStream.readUTF());
                databasesAdmin.addProduct(product);
            } else if (indicator.equalsIgnoreCase("salesRepresentative")) {
                SalesRep salesRep = new SalesRep();
                salesRep.setName(inputStream.readUTF());
                salesRep.setPhoneNumber(inputStream.readUTF());
                salesRep.setNameFirm(inputStream.readUTF());
                salesRep.setUsername(inputStream.readUTF());
                databasesAdmin.addSr(salesRep);
            }
        } catch (IOException exception) {
            ProjectExceptions.writeToFile(exception);
        }
    }

    @Override
    public void delete() {
        DatabasesAdmin databasesAdmin = new DatabasesAdmin( outputStream, inputStream);
        try {
            indicator = inputStream.readUTF();
            if (indicator.equalsIgnoreCase("product")) {
                Product product = new Product();
                product.setBrand(inputStream.readUTF());
                product.setModel(inputStream.readUTF());
                product.setPrice(inputStream.readUTF());
                product.setQuantity(inputStream.readUTF());
                databasesAdmin.deleteProduct(product);
            } else if (indicator.equalsIgnoreCase("salesRepresentative")) {
                SalesRep salesRep = new SalesRep();
                salesRep.setName(inputStream.readUTF());
                salesRep.setPhoneNumber(inputStream.readUTF());
                salesRep.setNameFirm(inputStream.readUTF());
                salesRep.setUsername(inputStream.readUTF());
                databasesAdmin.deleteSr(salesRep);
            }
        } catch (IOException exception) {
            ProjectExceptions.writeToFile(exception);
        }
    }

    @Override
    public void update() {
        DatabasesAdmin databasesAdmin = new DatabasesAdmin(outputStream, inputStream);
        try {
            indicator = inputStream.readUTF();

            if (indicator.equalsIgnoreCase("product")) {
                Product product = new Product();
                product.setBrand(inputStream.readUTF());
                product.setModel(inputStream.readUTF());
                product.setPrice(inputStream.readUTF());
                product.setQuantity(inputStream.readUTF());
                databasesAdmin.updateProduct(product);
            } else if (indicator.equalsIgnoreCase("salesRepresentative")) {
                SalesRep salesRep = new SalesRep();
                salesRep.setName(inputStream.readUTF());
                salesRep.setPhoneNumber(inputStream.readUTF());
                salesRep.setNameFirm(inputStream.readUTF());
                salesRep.setUsername(inputStream.readUTF());
                databasesAdmin.updateSr(salesRep);
            }
        } catch (IOException exception) {
           ProjectExceptions.writeToFile(exception);
        }
    }

    @Override
    public void refresh() {
        try {
            indicator = inputStream.readUTF();
        } catch (IOException exception) {
            ProjectExceptions.writeToFile(exception);
        }
        if (indicator.equalsIgnoreCase("product")) {
            this.query = "select  Brand, Model, Price, Quantity from products";
        } else if (indicator.equalsIgnoreCase("salesRepresentative")) {
            this.query = "select  Name, PhoneNumber, NameFirm, Username from sales_representative;";
        }
        new DatabasesAdmin( outputStream, inputStream).refresh(this.query);
    }

    public void analysis() {
        try {
            indicator = inputStream.readUTF();
        }catch (IOException exception){
            ProjectExceptions.writeToFile(exception);
        }
            if (indicator.equalsIgnoreCase("analysisTime")) {
            String timeReceived = null;
            String timeReceived1 = null;
            try {
                timeReceived = inputStream.readUTF();
                timeReceived1 = inputStream.readUTF();
            } catch (IOException exception) {
                ProjectExceptions.writeToFile(exception);
            }
            this.query = "SELECT Brand, Model, Username, TimeOfBuying, SaledQuantity, NameClient FROM purchase WHERE  TimeOfBuying BETWEEN '"
                    + timeReceived.toString() + "' AND '" + timeReceived1.toString() + "'";
        } else if (indicator.equalsIgnoreCase("analysisSr")) {
            String selectedName = null;
            try {
                selectedName = inputStream.readUTF();
            } catch (IOException exception) {
                ProjectExceptions.writeToFile(exception);
            }
            this.query = "SELECT Brand, Model, Username, TimeOfBuying, SaledQuantity, NameClient FROM purchase WHERE Username='" + selectedName + "'";

        }
        new DatabasesAdmin( outputStream, inputStream).refresh(this.query);
    }

    @Override
    public void visualize() {
        DatabasesVisualization repositoryVisualize = new DatabasesVisualization( inputStream, outputStream);
        try {
            indicator = inputStream.readUTF();
        } catch (IOException exception) {
            ProjectExceptions.writeToFile(exception);
        }
        switch (indicator){
            case"getAllNames": repositoryVisualize.getAllNames(); break;
            case"analyse": repositoryVisualize.createDataSetForChart(); break;
        }
    }


}


