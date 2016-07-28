package ru.smarttara.csvparsers;

import ru.smarttara.util.JdbcHelper;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CsvEmailParser {

    private static final int NOT_SENDED = 0;

    public static void main(String[] args) {
        parse(args[0]);

    }

    private static void parse(String filePath) {
        System.setProperty("file.encoding", "UTF-8");
        //zamenit' potom
//        filePath = "D:\\email_import.csv";
        Connection connection = JdbcHelper.getConnection();
//        PreparedStatement preparedStatement  = null;
        String sql = "INSERT INTO EMAILS(E_MAIL, IS_SENDED, COMPANY_NAME, PHONE_NUMBER, CATEGORY, ADDING_DATE, ADRESS, SITE) VALUES(?,?,?,?,?,now(),?,?)";
        String line = "";
        String csvSplitBy = ";";
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF8"));
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            while ((line = bufferedReader.readLine()) != null) {
                String[] emailData = line.split(csvSplitBy);
                preparedStatement.setString(1, emailData[0]); //E_MAIL
                preparedStatement.setInt(2, NOT_SENDED);//IS_SENDED
                preparedStatement.setString(3, emailData[1]);//COMPANY_NAME
                preparedStatement.setString(4, emailData[2]);//PHONE_NUMBER
                preparedStatement.setString(5, emailData[5]);//CATEGORY
                preparedStatement.setString(6, emailData[4]);//ADRESS
                preparedStatement.setString(7, emailData[3]);//SIT

                preparedStatement.executeUpdate();

                connection.commit();

                System.out.println(emailData.length);
                System.out.println("data " + emailData[0] + " " + emailData[1] + " " + emailData[5]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
            }
            e.printStackTrace();
        }
    }
}
