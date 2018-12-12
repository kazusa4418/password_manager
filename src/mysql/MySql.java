package mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;

@SuppressWarnings("unused")
public class MySql implements AutoCloseable {
    private String hostName;
    private String userName;
    private String password;
    private String databaseName;

    private boolean useSSL;
    private boolean autoReconnect;

    private String jdbcUri;

    private Connection connection;

    public MySql() throws SQLException {
        this("./mysql.properties");
    }

    public MySql(String propertyFilePath) throws SQLException {
        MySqlConfiguration.setPropertyFilePath(propertyFilePath);
        loadSettings();
        connection = DriverManager.getConnection(jdbcUri, userName, password);
    }

    public MySql(String hostName, String databaseName, String userName, String password) throws SQLException {
        System.out.println(hostName);
        this.hostName = hostName;
        this.databaseName = databaseName;
        this.userName = userName;
        this.password = password;
        this.useSSL = false;
        this.autoReconnect = true;
        this.jdbcUri = createJdbcUri();
        this.connection = DriverManager.getConnection(jdbcUri, userName, password);
    }

    private void loadSettings() {
        MySqlConfiguration.load();
        this.hostName = MySqlConfiguration.getHostName();
        this.userName = MySqlConfiguration.getUserName();
        this.password = MySqlConfiguration.getPassWord();
        this.databaseName = MySqlConfiguration.getDatabaseName();
        this.useSSL = MySqlConfiguration.getUseSSL();
        this.autoReconnect = MySqlConfiguration.getAutoConnect();
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

    public void begin() throws SQLException {
        connection.setAutoCommit(false);
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void reconnectTo(String hostName, String databaseName, String userName, String password) throws SQLException {
        close();
        this.hostName = hostName;
        this.databaseName = databaseName;
        this.userName = userName;
        this.password = password;
        this.jdbcUri = createJdbcUri();
        connection = DriverManager.getConnection(jdbcUri, userName, password);
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
        return jdbcUri;
    }

    public void close() {
        try {
            connection.close();
        }
        catch (SQLException ignore) {}
    }

    private String createJdbcUri() {
        StringBuilder sb = new StringBuilder("jdbc:mysql://");
        sb.append(hostName).append("/");

        if (!databaseName.isEmpty()) {
            sb.append(databaseName);
        }

        sb.append("&useSSL=").append(useSSL);
        sb.append("&autoReconnect=").append(autoReconnect);

        System.out.println(sb.toString());
        return sb.toString();
    }
}
