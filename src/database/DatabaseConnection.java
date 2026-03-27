package database;

import config.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY_MS = 2000;

    private final String url;
    private final String user;
    private final String password;
    private Connection connection;

    public DatabaseConnection(Config config) {
        this.url = config.getDbUrl();
        this.user = config.getDbUser();
        this.password = config.getDbPassword();
    }

    public Connection getConnection() throws SQLException {
        if(connection != null && !connection.isClosed()) {
            return connection;
        }

        int attempts  = 0;
        while(attempts < MAX_RETRIES) {
            try {
                connection = DriverManager.getConnection(url, user, password);
                return connection;
            } catch (SQLException e) {
                attempts++;
                System.err.println("Database connection attempt " + attempts + " failed: " + e.getMessage());
                if(attempts >= MAX_RETRIES) {
                    throw new SQLException("Could not connect to database after " + MAX_RETRIES + " attempts.", e);
                }
                try {
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException e1) {
                    Thread.currentThread().interrupt();
                    throw new SQLException("Interrupted while retrying database connection.", e1);
                }
            }
        }
        throw new SQLException("Could not connect to the database.");
    }

    public void close() throws SQLException{
        if(connection != null && !connection.isClosed()){
            connection.close();
        }
    }
}
