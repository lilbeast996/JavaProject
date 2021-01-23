package java_project_management_app_cp.commands;

import java_project_management_app_cp.services.SalesRepresentative;

public class CatalogCommand implements Command{
    private SalesRepresentative userType;

    public CatalogCommand(SalesRepresentative userType) {
        this.userType = userType;
    }

    @Override
    public void execute() {
        userType.catalog();
    }
}
