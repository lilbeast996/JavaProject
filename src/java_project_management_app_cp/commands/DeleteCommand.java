package java_project_management_app_cp.commands;

import java_project_management_app_cp.services.User;

public class DeleteCommand implements Command {
    private User userType;

    public DeleteCommand(User userType) {
        this.userType = userType;
    }

    @Override
    public void execute() {
        userType.delete();
    }
}
