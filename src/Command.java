import mysql.MySql;

import java.sql.SQLException;

class Command {
    static final MySql mysql = connectToMySql();

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
