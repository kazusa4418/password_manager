import util.Console;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Cp extends Command {
    public static void main(String[] args) {
        String serviceName = args[0];

        checkServiceName(serviceName);

        copyPasswordToClipboard(serviceName);
    }

    private static void checkServiceName(String serviceName) {
        try {
            ResultSet result = mysql.prepareStatement("SELECT COUNT(*) FROM passwords WHERE service = ?;")
                                    .set(serviceName)
                                    .executeQuery();

            if (!result.isBeforeFirst()) {
                System.err.println("fatal: service '" + serviceName + "' is not recorded.");
                System.exit(1);
            }
        }
        catch (SQLException err) {
            err.printStackTrace();
            System.exit(2);
        }
    }

    private static void copyPasswordToClipboard(String service) {
        try {
            ResultSet result = mysql.prepareStatement("SELECT password FROM passwords WHERE service = ?;")
                                    .set(service)
                                    .executeQuery();

            result.next();
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection ss = new StringSelection(result.getString("password"));

            clipboard.setContents(ss, ss);

            System.out.println("the password has been copied to clipboard.\nplease paste within 15 seconds.");

            Console.getInstance().sleep(15000);
        }
        catch (SQLException err) {
            err.printStackTrace();
            System.exit(1);
        }
    }
}
