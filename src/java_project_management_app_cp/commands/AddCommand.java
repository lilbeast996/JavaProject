package java_project_management_app_cp.commands;

import java_project_management_app_cp.services.User;

public class AddCommand implements Command {
    private User userType;

    public AddCommand(User userType) {
        this.userType = userType;
    }

    @Override
    public void execute() {
        userType.add();
    }
}
