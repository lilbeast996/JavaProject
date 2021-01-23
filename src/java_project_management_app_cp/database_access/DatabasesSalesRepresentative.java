package java_project_management_app_cp.database_access;

import java_project_management_app_cp.MailSending;
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

public class DatabasesSalesRepresentative {
    private String query;
    private Connection connection;
    private static DataOutputStream dos;
    private static DataInputStream dis;

    public DatabasesSalesRepresentative(Connection connection, DataOutputStream dos, DataInputStream dis) {
        this.connection = connection;
        this.dos = dos;
        this.dis = dis;
    }


    public void add(Client client){
        PreparedStatement ps = null;
        this.query = "insert into clients ( Name, PhoneNumber, Username) values( ?, ?, ?)";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, client.getName());
            ps.setString(2, client.getPhoneNumber());
            ps.setString(3, ServicesAccounts.getUsername());
            ps.executeUpdate();
            refresh("select * from clients where Username = '" + ServicesAccounts.getUsername() + "'");
        } catch (SQLException e) {

        }
    }

    public void update(Client client){
        PreparedStatement ps = null;
        this.query = "update  clients set Name = ? where PhoneNumber = ? ;";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, client.getName());
            ps.setString(2, client.getPhoneNumber());
            ps.executeUpdate();
            refresh("select * from clients where Username = '" + ServicesAccounts.getUsername() + "'");
        } catch (SQLException  e) {

        }
    }

    public void delete(Client client){
        PreparedStatement ps = null;
        this.query = "delete from clients where PhoneNumber = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, client.getPhoneNumber());
            ps.executeUpdate();
            refresh("select * from clients where Username = '" + ServicesAccounts.getUsername() + "'");
        } catch (SQLException  e) {

        }
    }

    public void refresh(String query){
        String executeQuery = query;
        try {
            PreparedStatement statement = connection.prepareStatement(executeQuery);
            ResultSet rs = statement.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();

            int numberOfColumns = metaData.getColumnCount();
            Vector columnNames = new Vector();
            for (int column = 0; column < numberOfColumns; column++) {
                columnNames.addElement(metaData.getColumnLabel(column + 1));
            }

            Vector rows = new Vector();
            while (rs.next()) {
                Vector newRow = new Vector();

                for (int i = 1; i <= numberOfColumns; i++) {
                    newRow.addElement(rs.getObject(i));
                }

                rows.addElement(newRow);
            }
            ObjectOutputStream ois = new ObjectOutputStream(dos);
            ois.writeObject(rows);
            ois.writeObject(columnNames);

        } catch (SQLException | IOException exception) {

        }
    }

    public void changeQuantityProduct(int newQuantity, String model)  {
        int quantityOld = 0;
        this.query = ("SELECT * FROM products WHERE Model = '" + model + "'");
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = statement.executeQuery(query);
            rs.beforeFirst();
            while(rs.next()) {
                quantityOld = Integer.parseInt(rs.getString("Quantity"));
            }
            if(quantityOld<=10){
                new MailSending().sendMail("firstmailproject@gmail.com", model);
            }
            if(newQuantity <=0 || newQuantity > quantityOld){
                dos.writeBoolean(false);
            }else{
                String queryProductChange = "update products set Quantity = Quantity - " + String.valueOf(newQuantity)+ " where Model = '" + model + "'";
                Statement statementNew = null;
                try {
                    statementNew = connection.createStatement();
                    statementNew.executeUpdate(queryProductChange);
                } catch (SQLException e) {

                }
                dos.writeBoolean(true);
            }
        } catch (SQLException | IOException ex) {

        }
    }

    public void pressButtonCatalogAdd(Purchase purchase) {
        PreparedStatement ps = null;
        this.query = "insert into purchase (IDPurchase, Brand, Model, Username, TimeOfBuying, SaledQuantity, NameClient) values(?, ?, ?, ?, ?, ?, ?)";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(2, purchase.getBrand());
            ps.setString(3, purchase.getModel());
            ps.setString(4, ServicesAccounts.getUsername());
            ps.setString(5, String.valueOf(LocalDate.now()));
            ps.setString(6, String.valueOf(purchase.getQuantity()));
            ps.setString(7, purchase.getNameClient());
            ps.setString(1, purchase.getIDPurchase());
            ps.executeUpdate();
        }catch (SQLException e){

        }
        this.query = "update  analysis set SaledQuantity = SaledQuantity + ?  where Username = ? ";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, String.valueOf(purchase.getQuantity()));
            ps.setString(2, ServicesAccounts.getUsername());
            ps.executeUpdate();
        }catch (SQLException e){

        }
    }
}
