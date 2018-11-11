package mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;

@SuppressWarnings("unused")
public class MySql implements AutoCloseable {
    private String hostName = MySqlConfiguration.getHostName();
    private String userName = MySqlConfiguration.getUserName();
    private String password = MySqlConfiguration.getPassWord();
    private String databaseName = MySqlConfiguration.getDatabaseName();

    private String useSSL = MySqlConfiguration.getUseSSL();
    private String autoReconnect = MySqlConfiguration.getAutoConnect();

    private String jdbcUrl = "jdbc:mysql://" + hostName + "/" + databaseName + "?autoReconnect=" + autoReconnect + "&useSSL=" + useSSL;

    private Connection connection;

    public MySql() throws SQLException {
        connection = DriverManager.getConnection(jdbcUrl, userName, password);
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        return connection.createStatement().executeQuery(sql);
    }

    public int executeUpdate(String sql) throws SQLException {
        return connection.createStatement().executeUpdate(sql);
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return new PreparedStatement(connection.prepareStatement(sql));
    }

    public String getHostName() {
        return hostName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void close() {
        try {
            connection.close();
        }
        catch (SQLException ignore) {}
    }
}
