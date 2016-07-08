package testingMail;

/**
 * Created by ZXCASD on 06.07.2016.
 */

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class TestMail {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.yandex.ru");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("talismanoff1990@yandex.ru","AleksanderT1");
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("talismanoff1990@yandex.ru"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("talismanoff1990@yandex.ru"));
            message.setSubject("Testing Subject");
            message.setText("Dear Mail Crawler," +
                    "\n\n No spam to my email, please!");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
