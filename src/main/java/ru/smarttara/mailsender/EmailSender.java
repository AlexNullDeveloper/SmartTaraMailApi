package ru.smarttara.mailsender;

import com.sun.javaws.exceptions.LaunchDescException;
import ru.smarttara.launcher.Launcher;
import ru.smarttara.mailing.MailFrame;
import ru.smarttara.mainFrame.MainFrame;
import ru.smarttara.mainFrame.Parameters;
import ru.smarttara.util.JdbcHelper;
import ru.smarttara.util.ProcessFrame;
import ru.smarttara.util.ProgressBarRunnable;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by a.talismanov on 07.07.2016.
 */
public class EmailSender {

    private static final String AUDIT_INSERT_SQL = "INSERT INTO SENDED_MAILS(E_MAIL_FROM, E_MAIL_TO, SENDING_TIME) VALUES (?,?,now())";

    public static void sendTestEmail(MailFrame mailFrame) {


        Launcher.logger.debug("sendTestEmail");

        Properties props = getProperties();

        Session session = getSession(Launcher.getEmailFrom(), Launcher.getPassword(), props);
        String mailTo = String.valueOf(mailFrame.getCheckBoxEmails().getSelectedItem());

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Launcher.getEmailFrom()));


            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mailTo));
            message.setSubject("Testing Subject");

            String companyName = "COMPANY NAME THAT REPLACES";

            String textOfMessage = MainFrame
                    .getApplicationParametersMap()
                    .get(Parameters.MAIL_TEXT_PARAM)
                    .replace(":company_name",companyName);//getMessageString();

            message.setText(textOfMessage);

            Transport.send(message);

            Launcher.logger.debug("Done sending testEmail");

        } catch (javax.mail.AuthenticationFailedException e) {
            Launcher.logger.fatal("Ошибка аутентификации", e);
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            Launcher.logger.fatal("MessagingException",e);
            throw new RuntimeException(e);
        }


        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JdbcHelper.getConnection();
            preparedStatement = connection.prepareStatement(AUDIT_INSERT_SQL);
            preparedStatement.setString(1, Launcher.getEmailFrom());
            preparedStatement.setString(2, mailTo);
            preparedStatement.executeUpdate();
            connection.commit();
            Launcher.logger.debug("after commit");
        } catch (SQLException e) {
            Launcher.logger.error("AUDIT INSERT EXCEPTION", e);
            try {
                connection.rollback();
            } catch (SQLException e1) {
                Launcher.logger.fatal("failed to rollback", e1);
                e1.printStackTrace();
            }
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException SQLe) {
                Launcher.logger.fatal("SQLException", SQLe);
                throw new RuntimeException("problem with closing resources");
            }
        }
    }

    public static void sendRealEmail(MailFrame mailFrame, ProcessFrame processFrame) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String countSql = "SELECT COUNT(*) FROM EMAILS WHERE IS_SENDED = 0";
        String sql = "SELECT E_MAIL FROM EMAILS WHERE IS_SENDED = 0";
        try {
            connection = JdbcHelper.getConnection();
            preparedStatement = connection.prepareStatement(countSql);
            resultSet = preparedStatement.executeQuery();
            double resultSetSize = 0;
            if (resultSet.next()) {
                resultSetSize = resultSet.getInt(1);
            }
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            double stepOfProgress = getStepOfProgress(resultSetSize);
            processFrame.setStepOfProgress(stepOfProgress);
            String e_mail = "";
            double progress = 0;
            while (resultSet.next()) {
                e_mail = resultSet.getString("E_MAIL");
                Launcher.logger.debug("e_mail we working with " + e_mail);

                Properties props = getProperties();


                Session session = getSession(Launcher.getEmailFrom(), Launcher.getPassword(), props);

                try {
                    //TODO вернуть назад после тестирования
                    sendMessageToEmail(session, e_mail);
                    Launcher.logger.debug("Успешно отправлено письмо на почту " + e_mail);
                    sql = "UPDATE EMAILS SET IS_SENDED = 1 WHERE E_MAIL = ?";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1,e_mail);
                    preparedStatement.executeUpdate();
                    preparedStatement = connection.prepareStatement(AUDIT_INSERT_SQL);
                    preparedStatement.setString(1, Launcher.getEmailFrom());
                    preparedStatement.setString(2, e_mail);
                    preparedStatement.executeUpdate();
                    connection.commit();
                    Launcher.logger.debug("добавили в таблицу почту " + e_mail);
                    EventQueue.invokeLater(new ProgressBarRunnable(processFrame, stepOfProgress));
                    Thread.sleep(30000);
                }
                catch (MessagingException e) {
                    Launcher.logger.fatal("MessagingException",e);
                    Launcher.logger.debug("e.getCause() " + e.getCause());
                    Launcher.logger.debug("e.getClass().getCanonicalName() " + e.getClass().getCanonicalName());
                    Launcher.logger.debug("e.getMessage() " + e.getMessage());
                    connection.rollback();
                    throw new RuntimeException(e);
                }
                catch (InterruptedException e) {
                    Launcher.logger.fatal("InterruptedException", e);
                }
            }
            EventQueue.invokeLater(() -> {
                processFrame.getjProgressBar().setValue(100);
                processFrame.dispose();
                JOptionPane.showMessageDialog(mailFrame, "Осуществлена реальная отправка",
                        "Успех", JOptionPane.INFORMATION_MESSAGE);
            });
            Launcher.logger.debug("закончились все почты");
        } catch (SQLException e) {
            Launcher.logger.error("Exception in EmailSender",e);
        }
    }

    private static double getStepOfProgress(double resultSetSize) {
        double stepOfProgress = 100.00;
        if (resultSetSize > 0) {
            stepOfProgress = 100.00 / resultSetSize;
        }
        return stepOfProgress;
    }

    private static void sendMessageToEmail(Session session, String email) throws MessagingException {
        Message message = new MimeMessage(session);
        setMessage(message, email);

//        String textOfMessage = MainFrame.getApplicationParametersMap().get(Parameters.MAIL_TEXT_PARAM); //getMessageString();
        Connection connection = JdbcHelper.getConnection();
        String sql = "SELECT COMPANY_NAME FROM EMAILS WHERE E_MAIL = ?";
        String companyName = "";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                companyName = resultSet.getString("e_mail");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        String textOfMessage = MainFrame
                .getApplicationParametersMap()
                .get(Parameters.MAIL_TEXT_PARAM)
                .replace(":company_name",companyName); //getMessageString();

        message.setText(textOfMessage);

        Transport.send(message);
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
        message.setSubject(MainFrame.getApplicationParametersMap().get(Parameters.EMAIL_HEADER_PARAM));
    }


    static Properties getProperties() {
        Properties props = new Properties();

        String smtphost = getSmtpHostByMail();

        props.put("mail.smtp.host", smtphost);
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


    public static void main(String[] args) {
        Properties props = getProperties();
        Session session = getSession(args[0], args[1], props);

        try {
            Message message = new MimeMessage(session);
            setMessage(message);
            String textOfMessage = MainFrame.getApplicationParametersMap().get(Parameters.MAIL_TEXT_PARAM); //getMessageString();
            message.setText(textOfMessage);
            Transport.send(message);

            Launcher.logger.debug("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

