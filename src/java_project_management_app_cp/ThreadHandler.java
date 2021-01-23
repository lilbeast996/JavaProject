package java_project_management_app_cp;

import java_project_management_app_cp.commands.*;
import java_project_management_app_cp.services.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

class ThreadHandler extends Thread {
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;
    private String indicator;
    private User user;
    private Admin userAdmin;
    private SalesRepresentative salesRepresentative;

    public ThreadHandler(DataInputStream inputStream, DataOutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        Command command = null;
        while(true){
            try {
                indicator = inputStream.readUTF();
            } catch (IOException exception) {
                ProjectExceptions.writeToFile(exception);
            }

            switch (indicator) {
                case "login":
                    new ServicesAccounts(inputStream, outputStream).login();
                    user = UserType.getUserType();
                    if(ServicesAccounts.getUser().equalsIgnoreCase("admin")){
                        userAdmin = UserType.getUserAdmin();
                    }else if(ServicesAccounts.getUser().equalsIgnoreCase("sr")){
                        salesRepresentative = UserType.getUserSalesRep();
                    }
                    break;
                case "add":
                    command = new AddCommand(user);
                    invokeButton(command);
                    break;
                case "delete":
                    command = new DeleteCommand(user);
                    invokeButton(command);
                    break;
                case "update":
                    command = new UpdateCommand(user);
                    invokeButton(command);
                    break;
                case "refresh":
                    command = new RefreshCommand(user);
                    invokeButton(command);
                    break;
                case "analysis":
                    command = new AnalysisCommand(userAdmin);
                    invokeButton(command);
                    break;
                case "visualizeComponents":
                    command = new VisualizeCommand(user);
                    invokeButton(command);
                    break;
                case "catalog":
                    command = new CatalogCommand(salesRepresentative);
                    invokeButton(command);
                    break;
            }

            if(indicator.equalsIgnoreCase("log out")){
                break;
            }
        }
    }

    private void invokeButton(Command command){
        ButtonInvoker onPress = new ButtonInvoker(command);
        onPress.click();
    }
}



