package utils;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


/**
 * Created by Juraj on 14.12.2015.
 */
public class MailSender {

    public void sendMessage(String receiver, String token) {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        String msgBody = "Please click on the link and confirm your account " +
                "http://www.frendz-1149.appspot.com/confirmAccount.jsp?email=" + receiver + "&token=" + token;

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("frendz-1149@appspot.gserviceaccount.com", "Team frendz"));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(receiver, "Dear User"));
            msg.setSubject("Your frendz.com account needs to be verified");
            msg.setText(msgBody);
            Transport.send(msg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
