package java_project_management_app_cp.services;

public class UserType {
    private static User userType;
    private static Admin userAdmin;
    private static SalesRepresentative userSalesRep;

    public static void setUserType(User userType) {
        UserType.userType = userType;
    }

    public static User getUserType() {
        return userType;
    }

    public static Admin getUserAdmin() {
        return userAdmin;
    }

    public static void setUserAdmin(Admin userAdmin) {
        UserType.userAdmin = userAdmin;
    }

    public static SalesRepresentative getUserSalesRep() {
        return userSalesRep;
    }

    public static void setUserSalesRep(SalesRepresentative userSalesRep) {
        UserType.userSalesRep = userSalesRep;
    }
}
