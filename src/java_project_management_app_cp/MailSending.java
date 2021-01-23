package java_project_management_app_cp;

import java_project_management_app_cp.services.ServicesAccounts;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailSending {

    public void sendMail(String recipient, String model){
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String accountEmail = "secondmailproject@gmail.com";
        String password = "Password1@";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(accountEmail, password);
            }
        });

        Message message = prepareMessage(session, accountEmail, recipient, model);

        try {
            Transport.send(message);
        } catch (MessagingException exception) {
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
