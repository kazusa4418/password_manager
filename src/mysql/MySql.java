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

    private boolean useSSL = MySqlConfiguration.getUseSSL();
    private boolean autoReconnect = MySqlConfiguration.getAutoConnect();

    private String jdbcUri = createJdbcUri();

    private Connection connection;

    public MySql() throws SQLException {
        connection = DriverManager.getConnection(jdbcUri, userName, password);
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
        sb.append(databaseName);

        if (useSSL) {
            sb.append("&useSSL=").append(useSSL);
            sb.append("&requireSSL=true");
            sb.append("&verifyServerCertificate=true");
            sb.append("&trustCertificateKeyStoreUrl=").append("file");
            sb.append("&trustCertificateKeyStoreType=").append("JKS");
            sb.append("&trustCertificateKeyStorePassword=").append("YOUR_JKS_PASSWORD");
        }
        if (autoReconnect) {
            sb.append("&autoReconnect=").append(autoReconnect);
        }

        return sb.toString();
    }
}
