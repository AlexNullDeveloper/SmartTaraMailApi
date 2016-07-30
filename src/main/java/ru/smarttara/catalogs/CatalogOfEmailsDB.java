package ru.smarttara.catalogs;


import ru.smarttara.util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * class that help to work with catalog of emails
 * Created by a.talismanov on 01.06.2016.
 */
class CatalogOfEmailsDB {

    /**
     * Connection to database
     * */
    private static Connection connect;

    /**
     * Retuirns resultSet with data from table EMAILS
     * @return ResultSet from table Emails
     */
    static ResultSet getResultSetFromTableEmails() {
        connect = JdbcHelper.getConnection();
        ResultSet rs = null;
        String selectDataFromEmails = "SELECT E_MAIL, IS_SENDED, COMPANY_NAME, PHONE_NUMBER, CATEGORY, ADDING_DATE " +
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
