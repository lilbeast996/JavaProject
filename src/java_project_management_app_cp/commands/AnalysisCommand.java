package java_project_management_app_cp.commands;


import java_project_management_app_cp.services.Admin;

public class AnalysisCommand implements Command{
    private Admin userType;

    public AnalysisCommand(Admin userType) {
        this.userType = userType;
    }

    @Override
    public void execute() {
        userType.analysis();
    }
}
