package java_project_management_app_cp.commands;

import java_project_management_app_cp.services.ServicesSalesRepresentative;

public class CatalogCommand implements Command{
    private ServicesSalesRepresentative userType;

    public CatalogCommand(ServicesSalesRepresentative userType) {
        this.userType = userType;
    }

    @Override
    public void execute() {
        userType.catalog();
    }
}
