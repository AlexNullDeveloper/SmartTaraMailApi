package mailsender;

import Launcher.Launcher;
import presenter.MailFrame;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by a.talismanov on 07.07.2016.
 */
public class EmailSender {


    public static void sendTestEmail(MailFrame mailFrame) {

        System.out.println("sendTestEmail");

        Properties props = getProperties();

        Session session = getSession(Launcher.getEmailFrom(), Launcher.getPassword(), props);

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Launcher.getEmailFrom()));

            String mailTo = String.valueOf(mailFrame.getCheckBoxEmails().getSelectedItem());

            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mailTo));
            message.setSubject("Testing Subject");

            String textOfMessage = getMessageString();

            message.setText(textOfMessage);

            Transport.send(message);

            System.out.println("Done");

        } catch (javax.mail.AuthenticationFailedException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendRealEmail(MailFrame mailFrame) {
        String mailsFilePath = "C:\\mail\\csv\\mails.csv";

        List<String> linesOfFile = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(mailsFilePath))) {
            linesOfFile = stream.collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, String> emails = new HashMap<>();

        linesOfFile.forEach(s -> {
            String[] tempArrOfStr = s.split(";");
            emails.put(tempArrOfStr[0], tempArrOfStr[1]);
        });

        Properties props = getProperties();


        Session session = getSession(Launcher.getEmailFrom(), Launcher.getPassword(), props);

        for (Map.Entry<String, String> email : emails.entrySet()) {

            String emailStr = email.getKey();
            String sendedOrNot = email.getValue();

            try {

                if (Integer.parseInt(sendedOrNot) == 0) {
                    sendMessageToEmail(session, emailStr);
                    sendedOrNot = "1";
                    emails.put(emailStr, sendedOrNot);
                }
                System.out.println("Успешно отправлено письмо на почту " + email);

            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }

            try (FileWriter fileWriter = new FileWriter(mailsFilePath)) {
                fileWriter.write(emailStr);
                fileWriter.write(";");
                fileWriter.write(sendedOrNot);
                fileWriter.write(";");
                fileWriter.write("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void sendMessageToEmail(Session session, String email) throws MessagingException {
        Message message = new MimeMessage(session);
        setMessage(message, email);

        String textOfMessage = getMessageString();

        message.setText(textOfMessage);

        Transport.send(message);
    }

    private static String getMessageString() {
        return "Компания Smarttara поможет Вам в разработке и изготовлении" +
                " промышленной и транспортной тары различной сложности.\n" +
                "\n" +
                "Осуществляем полный цикл разработки тары - от макета (3D-модели) " +
                "до готового изделия, а также, при необходимости, реализацию отдельных её стадий:\n" +
                "\n" +
                "* сбор и анализ требований (условия эксплуатации, транспортирования " +
                "и хранения, материалы, специальные требования);\n" +
                "\n" +
                "* разработка макета (3D-модель);\n" +
                "\n" +
                "* разработка конструкторской и эксплуатационной документации;\n" +
                "\n" +
                "* изготовление опытных образцов;\n" +
                "\n" +
                "* проведение испытаний (при необходимости).\n" +
                "\n" +
                "Разработка осуществляется в соответствии с ГОСТ и отраслевыми стандартами.\n" +
                "\n" +
                "Возможно использование различных датчиков для контроля сохранности грузов.\n" +
                "\n" +
                "Для оценки сроков и стоимости работ Вы можете оформить заявку на сайте " +
                "smarttara.ru или связаться с нами любым удобным способом:\n" +
                "\n" +
                "Телефон: 8-985-455-28-90\n" +
                "\n" +
                "Эл. почта: smarttara.inbox@gmail.com\n" +
                "\n" +
                "Сайт: smarttara.ru\n" +
                "\n" +
                "Контактное лицо: Александр, Павел.";
    }


    public static void main(String[] args) {
        Properties props = getProperties();

        Session session = getSession(args[0], args[1], props);

        try {
            Message message = new MimeMessage(session);
            setMessage(message);
            String textOfMessage = getMessageString();
            message.setText(textOfMessage);
            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private static Session getSession(String login, String password, Properties props) {
        return Session.getDefaultInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(login, password);
                    }
                });
    }

    private static void setMessage(Message message) throws MessagingException {
        setMessage(message, Launcher.getEmailFrom());
    }

    private static void setMessage(Message message, String emailTo) throws MessagingException {
        //TODO отправку с адреса заменить на основной
        message.setFrom(new InternetAddress(Launcher.getEmailFrom()));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(emailTo));
        //TODO заменить на правильное
        message.setSubject("Коммерческое предложение");
    }


    private static Properties getProperties() {
        Properties props = new Properties();

        String smtphost = getSmtpHostByMail();

        props.put("mail.smtp.host", smtphost);//было "smtp.gmail.com"
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        return props;
    }

    private static String getSmtpHostByMail() {

        String workingWith = Launcher.getEmailFrom();

        int index = workingWith.indexOf("@");

        return "smtp." + workingWith.substring(index + 1);
    }
}

