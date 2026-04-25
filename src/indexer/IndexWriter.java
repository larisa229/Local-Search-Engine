package indexer;

import database.DatabaseConnection;
import model.FileRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public class IndexWriter {
    private final DatabaseConnection connection;

    // if a row with the given path already exists, switch to update
    // if the insert fails, the data is kept by postgreSQL and labeled as EXCLUDED
    private static final String UPSERT_SQL = """
        INSERT INTO files (absolute_path, name, extension, size, last_modified, checksum, content_preview, content, path_score, search_vector, indexed_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, to_tsvector('english', ? || ' ' || LEFT(COALESCE(?, ''), 500000)), ?) 
                ON CONFLICT (absolute_path) DO UPDATE SET
                    name             = EXCLUDED.name,
                    extension        = EXCLUDED.extension,
                    size             = EXCLUDED.size,
                    last_modified    = EXCLUDED.last_modified,
                    checksum         = EXCLUDED.checksum,
                    content_preview  = EXCLUDED.content_preview,
                    content          = EXCLUDED.content,
                    path_score       = EXCLUDED.path_score,
                    search_vector    = to_tsvector('english', EXCLUDED.name || ' ' || LEFT(COALESCE(EXCLUDED.content, ''), 500000)),
                    indexed_at       = EXCLUDED.indexed_at
        """;

    public IndexWriter(DatabaseConnection connection) {
        this.connection = connection;
    }

    public void write(FileRecord record) throws SQLException {
        try {
            Connection conn = connection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(UPSERT_SQL)) {
                stmt.setString(1, record.getAbsolutePath());
                stmt.setString(2, record.getName());
                stmt.setString(3, record.getExtension());
                stmt.setLong(4, record.getSize());
                stmt.setLong(5, record.getLastModified());
                stmt.setString(6, record.getChecksum());
                stmt.setString(7, record.getContentPreview());
                stmt.setString(8, record.getContent());
                stmt.setDouble(9, record.getPathScore());
                stmt.setString(10, record.getName());
                stmt.setString(11, record.getContent());
                stmt.setTimestamp(12, Timestamp.from(Instant.now()));
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to write record for: " + record.getAbsolutePath() + " — " + e.getMessage(), e);
        }
    }
}
