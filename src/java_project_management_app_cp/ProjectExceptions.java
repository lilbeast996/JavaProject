package java_project_management_app_cp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

public class ProjectExceptions {

    public static void writeToFile(Exception exception){
        try {
            String filename="Exceptions\\ExceptionLogServer.txt";
            Path pathToFile = Paths.get(filename);
            byte bytes[] = ("\r\n"+ LocalDateTime.now() +"] : "+ exception.toString()).getBytes();
            Files.write(pathToFile, bytes, StandardOpenOption.APPEND);
            System.out.println("ProjectExceptions logged to your file");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
