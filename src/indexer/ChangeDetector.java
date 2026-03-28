package indexer;

import database.DatabaseConnection;

import java.sql.*;
import java.util.List;

public class ChangeDetector {
    private final DatabaseConnection dbConnection;

    private static final String SELECT_SQL = """
            SELECT checksum FROM files WHERE absolute_path = ?
            """;

    private static final String DELETE_STALE_SQL = """
            DELETE FROM files
            WHERE absolute_path <> ALL (?::text[])
            )
            """;

    public ChangeDetector(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public enum FileStatus {
        NEW,
        MODIFIED,
        UNCHANGED
    }

    public FileStatus getStatus(String absolutePath, String currentChecksum) throws SQLException {
        Connection conn = dbConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_SQL)) {
            stmt.setString(1, absolutePath);
            ResultSet rs = stmt.executeQuery();

            if(!rs.next()) {
                return FileStatus.NEW;
            }

            String storedChecksum = rs.getString("checksum");
            if(storedChecksum.equals(currentChecksum)) {
                return FileStatus.UNCHANGED;
            }

            return FileStatus.MODIFIED;
        }
    }

    public int removeDeletedFiles(List<String> presentPaths) throws SQLException {
        Connection conn = dbConnection.getConnection();
        Array pathArray = conn.createArrayOf("text", presentPaths.toArray());
        try (PreparedStatement stmt = conn.prepareStatement(DELETE_STALE_SQL)) {
            stmt.setArray(1, pathArray);
            int deleted = stmt.executeUpdate();
            if(deleted > 0) {
                System.out.println("Removed " + deleted + " deleted file(s) from index.");
            }
            return deleted;
        }
    }
}
