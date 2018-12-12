import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws Exception {
        String HOST_NAME = "localhost";
        String USER_NAME = "finne";
        String PASSWORD = "finne_admin";
        String uri = "jdbc:mysql://" + HOST_NAME + "/test";

        Connection connection = DriverManager.getConnection(uri, USER_NAME, PASSWORD);

        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE DATABASE test_database;");
        connection.close();

    }
}
