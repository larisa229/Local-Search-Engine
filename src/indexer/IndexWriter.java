import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IndexWriter {
    private final DatabaseConnection connection;

    private static final String UPSERT_SQL = """
            INSERT INTO files (absolute_path, name, extension, size, last_modified, checksum, content_preview, indexed_at)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    ON CONFLICT (absolute_path) DO UPDATE SET
                        name             = EXCLUDED.name,
                        extension        = EXCLUDED.extension,
                        size             = EXCLUDED.size,
                        last_modified    = EXCLUDED.last_modified,
                        checksum         = EXCLUDED.checksum,
                        content_preview  = EXCLUDED.content_preview,
                        indexed_at       = EXCLUDED.indexed_at
            """;

    public IndexWriter(DatabaseConnection connection) {
        this.connection = connection;
    }

    public void write(FileRecord record) throws SQLException {
        Connection conn = connection.getConnection();
        try(PreparedStatement stmt = conn.prepareStatement(UPSERT_SQL)) {
            stmt.setString(1, record.getAbsolutePath());
            stmt.setString(2, record.getName());
            stmt.setString(3, record.getExtension());
            stmt.setLong(4, record.getSize());
            stmt.setLong(5, record.getLastModified());
            stmt.setString(6, record.getChecksum());
            stmt.setString(7, record.getContentPreview());
            stmt.setLong(8, System.currentTimeMillis());
            stmt.executeUpdate();
        }
    }
}
