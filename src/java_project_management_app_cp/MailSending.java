package java_project_management_app_cp;

import java_project_management_app_cp.services.ServicesAccounts;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class MailSending {

    public void sendMail(String recipient, String model){
        Properties properties = new Properties();
        fetchConfig(properties);

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(properties.getProperty("mail.user"), properties.getProperty("mail.password"));
            }
        });

        Message message = prepareMessage(session, properties.getProperty("mail.user"), recipient, model);

        try {
            Transport.send(message);
        } catch (MessagingException exception) {
            ProjectExceptions.writeToFile(exception);
        }
    }

    private  void fetchConfig(Properties properties) {
        Path path = Paths.get("MailProperties\\mailConfig.txt");
        try (InputStream input = Files.newInputStream(path)) {
            properties.load(input);
        }
        catch (IOException exception){
            ProjectExceptions.writeToFile(exception);
        }
    }

    private  Message prepareMessage(Session session, String accountMail, String recipient, String model){
        try{
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(accountMail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Notification: Lack of quantity");
            message.setText("Hello admin_1, \n I want to inform you about a decreasing quantity of products\n" +
                    "Product details:"+ model +"!\n" + ServicesAccounts.getUsername());
            return message;
        } catch (AddressException exception) {
            ProjectExceptions.writeToFile(exception);
        } catch (MessagingException exception) {
            ProjectExceptions.writeToFile(exception);
        }
        return null;
    }
}
