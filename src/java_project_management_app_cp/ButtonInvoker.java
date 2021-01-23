package java_project_management_app_cp;

import java_project_management_app_cp.commands.Command;

public class ButtonInvoker {
    private Command theCommand;

    public ButtonInvoker(Command theCommand) {
        this.theCommand = theCommand;
    }

    public void click(){
        theCommand.execute();
    }
}
