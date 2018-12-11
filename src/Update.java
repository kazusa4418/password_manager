import java.sql.ResultSet;
import java.sql.SQLException;

public class Update extends Command {
    public static void main(String[] args) {
        String serviceName = args[0];

        checkServiceName(serviceName);
        String password = inputPassword(serviceName);

        updateServicePassword(serviceName, password);
    }

    private static void checkServiceName(String serviceName) {
        try {
            ResultSet result = mysql.prepareStatement("SELECT COUNT(*) FROM passwords WHERE service = ?;")
                                    .set(serviceName)
                                    .executeQuery();

            if (!result.isBeforeFirst()) {
                console.println("fatal: service '" + serviceName + "' is not recorded.");
                System.exit(1);
            }
        }
        catch (SQLException err) {
            err.printStackTrace();
            System.exit(2);
        }
    }

    private static void updateServicePassword(String service, String password) {
        try {
            mysql.prepareStatement("UPDATE passwords SET password = ? WHERE service = ?;")
                    .set(password)
                    .set(service)
                    .executeUpdate();

            console.println("the password was updated.");
        }
        catch (SQLException err) {
            err.printStackTrace();
            System.exit(1);
        }
    }

    private static String inputPassword(String serviceName) {
        while (true) {
            console.print("register the password for the service '" + serviceName + "'.");
            String pw = console.readPassword("Enter " + serviceName + " password: ");
            String again = console.readPassword("Retype " + serviceName + " password: ");

            if (!pw.equals(again)) {
                console.println("Sorry, passwords do not match");
                continue;
            }
            return pw;
        }
    }
}
