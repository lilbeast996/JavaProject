package java_project_management_app_cp.commands;


import java_project_management_app_cp.services.ServicesAdmin;

public class AnalysisCommand implements Command{
    private ServicesAdmin userType;

    public AnalysisCommand(ServicesAdmin userType) {
        this.userType = userType;
    }

    @Override
    public void execute() {
        userType.analysis();
    }
}
