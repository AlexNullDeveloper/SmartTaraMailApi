package workers;

import appexceptions.DublicateMailException;
import launcher.Launcher;
import util.JdbcHelper;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by ZXCASD on 08.07.2016.
 */
public class MailDBWorker {
    private static final String DUBLICATE_KEY_VALUE_ERROR = "23505";
    public static void addMailInDataBase(String mail) {
        Connection connection = null;
        try {
            connection = JdbcHelper.getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException | URISyntaxException e) {
            Launcher.logger.fatal("something wrong with Connection",e);
            throw new RuntimeException(e);
        }

        String sql = "INSERT INTO EMAILS(E_MAIL, IS_SENDED) VALUES (?,0)";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,mail);
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
