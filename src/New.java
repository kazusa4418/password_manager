import util.Console;

import java.sql.ResultSet;
import java.sql.SQLException;

public class New extends Command {
    public static void main(String[] args) {
        String serviceName = args[0];

        checkServiceName(serviceName);
        String password = inputPassword(serviceName);

        recordNewService(serviceName, password);
    }

    private static void checkServiceName(String serviceName) {
        try {
            ResultSet result = mysql.prepareStatement("SELECT * FROM passwords WHERE service = ?;")
                                    .set(serviceName)
                                    .executeQuery();

            if (result.isBeforeFirst()) {
                System.err.println("fatal: service '" + serviceName + "' has already been recorded.");
                System.exit(1);
            }
        }
        catch (SQLException err) {
            err.printStackTrace();
            System.exit(2);
        }
    }

    private static void recordNewService(String service, String password) {
        try {
            mysql.prepareStatement("INSERT INTO passwords VALUES(?, ?);")
                    .set(service)
                    .set(password)
                    .executeUpdate();

            System.out.println("the password was recorded.");
        }
        catch (SQLException err) {
            err.printStackTrace();
            System.exit(1);
        }
    }

    private static String inputPassword(String serviceName) {
        Console console = Console.getInstance();

        while (true) {
            String pw = console.readPassword("Enter " + serviceName + " password: ");
            String again = console.readPassword("Retype " + serviceName + " password: ");

            if (!pw.equals(again)) {
                System.out.println("Sorry, passwords do not match");
                continue;
            }
            return pw;
        }
    }
}
