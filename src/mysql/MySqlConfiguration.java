package mysql;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import util.JLogger;
import java.util.logging.Level;

class MySqlConfiguration {
    private static String propertyFilePath;

    private static String hostName;
    private static String userName;
    private static String password;
    private static String databaseName;
    private static String useSSL;
    private static String autoConnect;

    static void setPropertyFilePath(String path) {
        propertyFilePath = path;
    }

    static void load() {
        loadMysqlProperties();
    }

    private static void loadMysqlProperties() {
        File file = new File(propertyFilePath);

        try (PropertyReader reader = new PropertyReader(file)) {
            reader.load();
            hostName = reader.getProperty("hostName");
            userName = reader.getProperty("userName");
            password = reader.getProperty("password");
            databaseName = reader.getProperty("databaseName");
            useSSL = reader.getProperty("useSSL", "false");
            autoConnect = reader.getProperty("autoReconnect", "false");
        }
        catch (FileNotFoundException err) {
            JLogger.log(Level.SEVERE, "mysql.properties is not found.", err);
            System.exit(3);
        }
        catch (IOException err) {
            JLogger.log(Level.SEVERE, "can not read mysql.properties.", err);
            System.exit(3);
        }
    }

    static String getHostName() {
        return hostName;
    }

    static String getUserName() {
        return userName;
    }

    static String getPassWord() {
        return password;
    }

    static String getDatabaseName() {
        return databaseName;
    }

    static boolean getUseSSL() {
        return Boolean.valueOf(useSSL);
    }

    static boolean getAutoConnect() {
        return Boolean.valueOf(autoConnect);
    }
}
