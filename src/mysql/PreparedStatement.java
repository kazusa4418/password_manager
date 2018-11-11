package mysql;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PreparedStatement {
    private int index = 1;
    private java.sql.PreparedStatement statement;

    PreparedStatement(java.sql.PreparedStatement statement) {
        this.statement = statement;
    }

    public PreparedStatement set(String value) throws SQLException {
        statement.setString(index++, value);
        return this;
    }

    public PreparedStatement set(boolean value) throws SQLException {
        statement.setBoolean(index++, value);
        return this;
    }

    public void executeUpdate() throws SQLException {
        statement.executeUpdate();
    }

    public ResultSet executeQuery() throws SQLException {
        return statement.executeQuery();
    }
}
