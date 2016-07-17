package ru.smarttara.workers;

import ru.smarttara.appexceptions.DublicateMailException;
import ru.smarttara.launcher.Launcher;
import ru.smarttara.util.JdbcHelper;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by ZXCASD on 08.07.2016.
 */
public class MailDBWorker {
    private static final String DUBLICATE_KEY_VALUE_ERROR = "23505";
    public static void addMailInDataBase(String mail, String companyName) {
        Connection connection = JdbcHelper.getConnection();

        String sql = "INSERT INTO EMAILS(E_MAIL, IS_SENDED, COMPANY_NAME) VALUES (?,0,?)";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,mail);
            preparedStatement.setString(2,companyName);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {

            Launcher.logger.debug("e.getMessage()" + e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
//            Launcher.logger.debug("e.getSQLState()" + e.getSQLState());
            if (e.getSQLState().equals(DUBLICATE_KEY_VALUE_ERROR)) {
                Launcher.logger.error("DublicateMailException",e);
                throw new DublicateMailException(e.getMessage());
            }
        }
        Launcher.logger.debug("insert transaction finished. Everything is fine");
    }
}
