package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Config {
    private final Properties properties;

    public Config(String configFilePath) {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(configFilePath)) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("Could not load config file: " + e.getMessage());
            System.err.println("Using default values.");
        }
    }

    public String getRootDirectory() {
        return properties.getProperty("root.directory", "./");
    }

    public List<String> getIgnoreDirectories() {
        String values = properties.getProperty("ignore.directories", ".git,.idea,node_modules,target,out");
        return Arrays.asList(values.split(","));
    }

    public List<String> getIgnoreExtensions() {
        String value = properties.getProperty("ignore.extensions", ".class,.jar,.png,.pdf,.zip");
        return Arrays.asList(value.split(","));
    }

    public String getDbUrl() {
        return properties.getProperty("db.url", "jdbc:postgresql://localhost:5432/search_engine");
    }

    public String getDbUser() {
        return properties.getProperty("db.user", "postgres");
    }

    public String getDbPassword() {
        return properties.getProperty("db.password", "");
    }
}
