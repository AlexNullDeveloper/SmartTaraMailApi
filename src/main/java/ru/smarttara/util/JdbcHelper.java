package ru.smarttara.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * @author a.talismanov  on 07.07.2016.
 */
public class JdbcHelper {

    private JdbcHelper() {
    }

    private static Logger log = Logger.getLogger(JdbcHelper.class.getName());

    public static void main(String[] args) throws SQLException, ClassNotFoundException, URISyntaxException {


        BasicConfigurator.configure();

        //TODO достучаться таки до СУБД

        String urlDB = "jdbc:postgresql://ec2-54-228-219-2.eu-west-1.compute.amazonaws.com:5432/d9uj3i9nqu6vmq?" +
                "sslmode=require&user=ttolakdstrfupa&password=Nn2Lvm_Lqrjz3RP1qsEJY5loTh";

        Class.forName("org.postgresql.Driver");
//        Connection connection = DriverManager.getConnection(urlDB,"ttolakdstrfupa","Nn2Lvm_Lqrjz3RP1qsEJY5loTh");


        Connection connection = getConnection();

        log.debug("is OK");
    }

    public static Connection getConnection() {

        log.info("getConnection...");

        URI dbUri = null;
        try {
            dbUri = new URI(System.getenv("DATABASE_URL"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        log.debug("dbUri " + dbUri);


        log.debug("before split");

        String username = dbUri
                .getUserInfo()
                .split(":")[0];
        String password = dbUri
                .getUserInfo()
                .split(":")[1];

        StringBuilder stringBuilderDbUrl = new StringBuilder();

        stringBuilderDbUrl.append("jdbc:postgresql://")
                .append(dbUri.getHost())
                .append(':')
                .append(dbUri.getPort())
                .append(dbUri.getPath())
                .append("?sslmode=require&user=")
                .append(username)
                .append("&password=")
                .append(password);

        String dbUrl = stringBuilderDbUrl.toString();

        log.debug("DB URL = " + dbUrl);

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dbUrl, username, password);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

}
