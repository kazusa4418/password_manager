import mysql.MySql;
import util.Console;

import java.sql.SQLException;

class Command {
    static final MySql mysql = connectToMySql();
    static final Console console = Console.getInstance();

    private static MySql connectToMySql() {
        try {
            return new MySql();
        }
        catch (SQLException err) {
            err.printStackTrace();
        }
        throw new AssertionError();
    }
}
