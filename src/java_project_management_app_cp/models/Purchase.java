package java_project_management_app_cp.models;

public class Purchase {
    private String IDPurchase;
    private String brand;
    private String model;
    private int quantity;
    private String nameClient;

    public String getIDPurchase() {
        return IDPurchase;
    }

    public void setIDPurchase(String IDPurchase) {
        this.IDPurchase = IDPurchase;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }
}
