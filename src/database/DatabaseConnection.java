import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/search_engine";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres1234";

    private Connection connection;

    public Connection getConnection() throws SQLException {
        if(connection == null || connection.isClosed()){
            connection = DriverManager.getConnection(URL,USER,PASSWORD);
        }
        return connection;
    }

    public void close() throws SQLException{
        if(connection != null && !connection.isClosed()){
            connection.close();
        }
    }
}
