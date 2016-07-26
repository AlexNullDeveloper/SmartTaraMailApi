package ru.smarttara.catalogs;

/**
 * Created by ZXCASD on 17.07.2016.
 */

import ru.smarttara.util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * class that help to work with catalog of emails
 * Created by a.talismanov on 01.06.2016.
 */
public class CatalogOfEmailsDB {

    /**
     * Connection to database
     * */
    private static Connection connect;

    /**
     * Retuirns resultSet with data from table EMAILS
     * @return ResultSet from table Emails
     */
    public static ResultSet getResultSetFromTableEmails() {
        connect = JdbcHelper.getConnection();
        ResultSet rs = null;
        String selectDataFromEmails = "SELECT E_MAIL, IS_SENDED, COMPANY_NAME, PHONE_NUMBER, CATEGORY_ID, ADDING_DATE " +
                "FROM EMAILS";


        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connect.prepareStatement(selectDataFromEmails);
            rs = preparedStatement.executeQuery();

        } catch (SQLException SQLe) {
            SQLe.printStackTrace();
        }

        return rs;
    }

}
