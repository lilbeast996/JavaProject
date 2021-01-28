package java_project_management_app_cp.services;

public class UserType {
    private static User userType;
    private static ServicesAdmin userAdmin;
    private static ServicesSalesRepresentative userSalesRep;

    public static void setUserType(User userType) {
        UserType.userType = userType;
    }

    public static User getUserType() {
        return userType;
    }

    public static ServicesAdmin getUserAdmin() {
        return userAdmin;
    }

    public static void setUserAdmin(ServicesAdmin userAdmin) {
        UserType.userAdmin = userAdmin;
    }

    public static ServicesSalesRepresentative getUserSalesRep() {
        return userSalesRep;
    }

    public static void setUserSalesRep(ServicesSalesRepresentative userSalesRep) {
        UserType.userSalesRep = userSalesRep;
    }
}
