package util;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

/**
 * @author a.talismanov  on 07.07.2016.
 */
public class JdbcHelper {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, URISyntaxException {

        //TODO достучаться таки до СУБД

        String urlDB = "jdbc:postgresql://ec2-54-228-219-2.eu-west-1.compute.amazonaws.com:5432/d9uj3i9nqu6vmq?" +
                "sslmode=require&user=ttolakdstrfupa&password=Nn2Lvm_Lqrjz3RP1qsEJY5loTh";

        Class.forName("org.postgresql.Driver");
//        Connection connection = DriverManager.getConnection(urlDB,"ttolakdstrfupa","Nn2Lvm_Lqrjz3RP1qsEJY5loTh");


        Connection connection = getConnection();

        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("DROP TABLE t1");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("DROP TABLE PROBLEM");
        }
        preparedStatement = connection.prepareStatement("CREATE TABLE t1(ID INTEGER)");
        preparedStatement.executeUpdate();
        System.out.println("after create");
        preparedStatement = connection.prepareStatement("INSERT INTO t1 VALUES (1)");
        System.out.println("after insert");
        preparedStatement.executeUpdate();
        connection.commit();
        preparedStatement = connection.prepareStatement("SELECT * FROM t1");
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            System.out.println(resultSet.getInt(1));
        }
        System.out.println("is OK");
    }

    public static Connection getConnection() throws URISyntaxException, SQLException {

        System.out.println(System.getenv("DATABASE_URL"));

        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        System.out.println(dbUri);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];

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

        System.out.println("DB URL = " + dbUrl);

        Connection connection = DriverManager.getConnection(dbUrl, username, password);
        connection.setAutoCommit(false);

        return connection;
    }

}
