import mysql.MySql;

import java.sql.SQLException;

public class test {
    private static String serviceName;

    public static void main(String[] args) {
        serviceName = args[0];

        String password = inputPassword();

        try (MySql mysql = new MySql()) {
            mysql.prepareStatement("INSERT INTO passwords VALUES(?, ?);")
                    .set(serviceName)
                    .set(password)
                    .executeQuery();
        }
        catch (SQLException err) {
            err.printStackTrace();
            System.exit(1);
        }

        System.out.println("the password was recorded.");
    }

    private static String inputPassword() {
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
