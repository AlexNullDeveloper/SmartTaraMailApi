import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author a.talismanov  on 07.07.2016.
 */
public class testingJDBC {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, URISyntaxException {
        String urlDB = "jdbc:postgresql://ec2-54-228-219-2.eu-west-1.compute.amazonaws.com:5432/d9uj3i9nqu6vmq?" +
                "sslmode=require&user=ttolakdstrfupa&password=Nn2Lvm_Lqrjz3RP1qsEJY5loTh";

        Class.forName("org.postgresql.Driver");
//        Connection connection = DriverManager.getConnection(urlDB,"ttolakdstrfupa","Nn2Lvm_Lqrjz3RP1qsEJY5loTh");


        Connection connection = getConnection();

//        PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE t1(ID INTEGER)");
//        preparedStatement.executeUpdate();
        System.out.println("is OK");
    }

    private static Connection getConnection() throws URISyntaxException, SQLException {

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

//        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
//
//
//        dbUrl += "?sslmode=require&user="+username+"&password=" + password;

        String dbUrl = stringBuilderDbUrl.toString();

        System.out.println("DB URL = " + dbUrl);

        return DriverManager.getConnection(dbUrl, username, password);
    }

}
