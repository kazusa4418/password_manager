package mysql;

import java.io.InputStreamReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader implements AutoCloseable {
    private InputStreamReader input;
    private Properties properties;

    public PropertyReader(File file) throws FileNotFoundException {
        this.properties = new Properties();
        this.input = new InputStreamReader(new FileInputStream(file));
    }

    public void load() throws IOException {
        properties.load(input);
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public void close() {
        properties = null;
        try {
            input.close();
        }
        catch (IOException ignore) {}
    }
}
