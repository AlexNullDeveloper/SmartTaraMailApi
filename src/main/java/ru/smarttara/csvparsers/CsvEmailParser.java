package ru.smarttara.csvparsers;

import ru.smarttara.util.JdbcHelper;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class CsvEmailParser {

    private static final int NOT_SENDED = 0;

    public static void main(String[] args) {
        parse(args[0]);

    }

    private static void parse(String filePath) {
        //zamenit' potom
//        filePath = "D:\\email_import_2.csv";
        Connection connection = JdbcHelper.getConnection();
        Set<String> uniqueMails = new HashSet<>();

        String sqlSelectMails = "SELECT e_mail FROM emails";
//        PreparedStatement preparedStatement  = null;
        String sql = "INSERT INTO EMAILS(E_MAIL, IS_SENDED, COMPANY_NAME, PHONE_NUMBER, CATEGORY, ADDING_DATE, ADRESS, SITE) VALUES(?,?,?,?,?,now(),?,?)";
        String line = "";
        String csvSplitBy = ";";
        System.out.println("filepath = " + filePath);


        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "Cp1251"));
//             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
             PreparedStatement preparedStatementSelect = connection.prepareStatement(sqlSelectMails);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatementSelect.executeQuery();
            while (resultSet.next()) {
                uniqueMails.add(resultSet.getString(1));
            }


            while ((line = bufferedReader.readLine()) != null) {
                String[] emailData = line.split(csvSplitBy);


                //trimming phone
                emailData[2] = emailData[2].trim().replaceAll(" ","").replaceAll("-","");
//                System.out.println("Phone " + emailData[2]);
//                System.out.println("Phone.length " + emailData[2].length());

                if (uniqueMails.contains(emailData[0])) {
                    System.out.println("dublicate email..." + emailData[0] + " skipping this");
                    continue;
                }


                System.out.println("data " + emailData[0] + " " + emailData[1] + " " + emailData[2] + " " + emailData[3] + " " + emailData[4] + " " + emailData[5]);
                preparedStatement.setString(1, emailData[0]); //E_MAIL
                preparedStatement.setInt(2, NOT_SENDED);//IS_SENDED
                preparedStatement.setString(3, emailData[1]);//COMPANY_NAME
                preparedStatement.setString(4, emailData[2]);//PHONE_NUMBER
                preparedStatement.setString(5, emailData[5]);//CATEGORY
                preparedStatement.setString(6, emailData[4]);//ADRESS
                preparedStatement.setString(7, emailData[3]);//SIT

                preparedStatement.executeUpdate();
                uniqueMails.add(emailData[0]);

                System.out.println(emailData.length);

            }
            connection.commit();
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
