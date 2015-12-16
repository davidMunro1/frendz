package utils;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.Security;
import java.util.Properties;
import java.util.logging.Logger;
import javax.activation.*;

/**
 * Created by Juraj on 14.12.2015.
 */
public class MailSender {

//    private String to = "orszag.juraj@gmail.com";
//    private static final String FROM = "orszag.juraj@gmail.com";
//    private static String host = "localhost";

    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;

//    public void sendMessage() {
//        // Get system properties
//        Properties properties = System.getProperties();
//
//        // Setup mail server
//        properties.setProperty("mail.smtp.host", host);
//        Properties props = new Properties();
//        props.setProperty("mail.smtp.host", "smtp.gmail.com");
//        props.setProperty("mail.smtp.port", "587");
//        Session session = Session.getDefaultInstance(props);
//
//        try{
//            // Create a default MimeMessage object.
//            MimeMessage message = new MimeMessage(session);
//
//            // Set From: header field of the header.
//            message.setFrom(new InternetAddress(FROM));
//
//            // Set To: header field of the header.
//            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
//
//            // Set Subject: header field
//            message.setSubject("This is the Subject Line!");
//
//            // Now set the actual message
//            message.setText("This is actual message");
//
//            // Send message
//            Transport.send(message);
//            System.out.println("Sent message successfully....");
//        }catch (MessagingException mex) {
//            mex.printStackTrace();
//        }
//
//        try {
//            // Step1
//            System.out.println("\n 1st ===> setup Mail Server Properties..");
//            mailServerProperties = System.getProperties();
//            mailServerProperties.put("mail.smtp.port", "587");
//            mailServerProperties.put("mail.smtp.auth", "true");
//            mailServerProperties.put("mail.smtp.starttls.enable", "true");
//            System.out.println("Mail Server Properties have been setup successfully..");
//
//            // Step2
//            System.out.println("\n\n 2nd ===> get Mail Session..");
//            getMailSession = Session.getDefaultInstance(mailServerProperties, null);
//            generateMailMessage = new MimeMessage(getMailSession);
//            generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress("orszag.juraj@gmail.com"));
//            generateMailMessage.setSubject("Greetings from Crunchify..");
//            String emailBody = "Test email by Crunchify.com JavaMail API example. " + "<br><br> Regards, <br>Crunchify Admin";
//            generateMailMessage.setContent(emailBody, "text/html");
//            System.out.println("Mail Session has been created successfully..");
//
//            // Step3
//            System.out.println("\n\n 3rd ===> Get Session and Send mail");
//            Transport transport = getMailSession.getTransport("smtp");
//
//            // Enter your correct gmail UserID and Password
//            // if you have 2FA enabled then provide App Specific Password
//            transport.connect("smtp.gmail.com", "orszag.juraj@gmail.com", "malinajeok");
//            transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
//            transport.close();
//        } catch (AddressException ex) {
//            ex.printStackTrace();
//        } catch (MessagingException ex) {
//            ex.printStackTrace();
//        }
        // Recipient's email ID needs to be mentioned.
//            String host = "smtp.gmail.com";
//            Properties props = System.getProperties();
//            props.put("mail.smtp.starttls.enable", "true");
//            props.put("mail.smtp.host", host);
//            props.put("mail.smtp.user", "orszag.juraj@gmail.com");
//            props.put("mail.smtp.password", "malinajeok");
//            props.put("mail.smtp.port", 587);
//            props.put("mail.smtp.auth", "true");
//            Session session = Session.getDefaultInstance(props, null);
//            MimeMessage mm = new MimeMessage(session);
//            try {
//                mm.setFrom(new InternetAddress("orszag.juraj@gmail.com"));
//                InternetAddress toAddress = new InternetAddress("orszag.juraj@gmail.com");
//                mm.addRecipient(Message.RecipientType.TO, toAddress);
//                mm.setSubject("Welcome to SweetBank");
//
//                mm.setText("Tjena");
//                Transport transport = session.getTransport("smtp");
//                transport.connect(host, "orszag.juraj@gmail.com", "malinajeok");
//                transport.sendMessage(mm, mm.getAllRecipients());
//                transport.close();
//
//            } catch (MessagingException ex) {
////                Logger.getLogger(FrmRegisterUser.class.getName()).log(Level.SEVERE, null, ex);
//                ex.printStackTrace();
//            }

//    }

    public void sendMessage() {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        String msgBody = "...";

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("orszag.juraj@gmail.com", "Example.com Admin"));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress("orszag.juraj@gmail.com", "Mr. User"));
            msg.setSubject("Your Example.com account has been activated");
            msg.setText(msgBody);
            Transport.send(msg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
