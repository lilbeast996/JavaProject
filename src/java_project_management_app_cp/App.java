package java_project_management_app_cp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import static java_project_management_app_cp.ProjectExceptions.writeToFile;

public class App {
    public static Socket s;
    public static DataInputStream inputStream;
    public static DataOutputStream outputStream;


    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(5056);
        } catch (IOException ex) {
            writeToFile(ex);
        }
        while (true) {
            try {
                s = serverSocket.accept();
                System.out.println("A new client is connected : " + s);
                System.out.println("Assigning new thread for this client.\n");

                inputStream = new DataInputStream(s.getInputStream());
                outputStream = new DataOutputStream(s.getOutputStream());

                Thread t = new ThreadHandler(inputStream, outputStream);
                t.start();
                t.sleep(100);

            } catch (SocketException exception) {
                if (s != null) {
                    s.close();
                }
                writeToFile(exception);
            } catch (IOException exception) {
                writeToFile(exception);
            } catch (InterruptedException exception) {
                writeToFile(exception);
            }
        }
    }
}
