package java_project_management_app_cp.commands;

import java_project_management_app_cp.services.User;

public class UpdateCommand implements Command {
    private User userType;

    public UpdateCommand(User userType) {
        this.userType = userType;
    }

    @Override
    public void execute() {
        userType.update();
    }
}
