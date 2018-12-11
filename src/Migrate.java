import mysql.MySql;
import mysql.PropertyReader;
import util.Console;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Migrate {
    public static void main(String... args) {
        Console console = Console.getInstance();
        String hostName = args[0];
        String userName = args[1];
        String password = args[2];

        try (MySql mysql = new MySql(hostName, "", userName, password);
             PropertyReader reader = new PropertyReader(new File("./mysql.properties"))) {

            hostName = reader.getProperty("hostName");
            String databaseName = reader.getProperty("databaseName");
            userName = reader.getProperty("userName");
            password = reader.getProperty("password");

            mysql.begin();
            mysql.executeUpdate("CREATE DATABASE " + databaseName + ";");
            console.println("created database successfully.");
            mysql.executeUpdate("CREATE USER " + userName + " identified by '" + password + "';");
            console.println("created user successfully.");
            mysql.executeUpdate("GRANT ALL PRIVILEGES ON " + databaseName + ".* TO " + userName + "@localhost;");
            mysql.executeUpdate("FLUSH PRIVILEGES;");
            console.println("set the authority for the user successfully.");
            mysql.commit();

            mysql.reconnectTo(hostName, databaseName, userName, password);

            mysql.executeUpdate(
                    "CREATE TABLE passwords(" +
                            "service_name varchar(128) primary key," +
                            "password varchar(128))");
            console.println("created table successfully.");

        }
        catch (SQLException err) {
            switch (err.getErrorCode()) {
                case 1698:
                    System.exit(1);
                    break;
                default:
                    System.err.println("error code: " + err.getErrorCode());
                    err.printStackTrace();
                    break;
            }
        }
        catch (IOException err) {
            err.printStackTrace();
        }
    }
}
