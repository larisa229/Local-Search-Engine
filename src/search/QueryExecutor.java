import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QueryExecutor {
    private final DatabaseConnection dbConnection;

    private static final String SEARCH_SQL = """
            SELECT name, absolute_path, extension, size, content_preview,
                           ts_rank(to_tsvector('english', name || ' ' || COALESCE(content_preview, '')),
                                   to_tsquery('english', ?)) AS rank
                    FROM files
                    WHERE to_tsvector('english', name || ' ' || COALESCE(content_preview, ''))
                          @@ to_tsquery('english', ?)
                    ORDER BY rank DESC
            """;

    public QueryExecutor(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public List<SearchResult> execute(String parsedQuery) throws SQLException {
        List<SearchResult> results = new ArrayList<>();
        Connection conn = dbConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(SEARCH_SQL)) {
            stmt.setString(1, parsedQuery);
            stmt.setString(2, parsedQuery);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(new SearchResult(
                        rs.getString("name"),
                        rs.getString("absolute_path"),
                        rs.getString("extension"),
                        rs.getLong("size"),
                        rs.getString("content_preview"),
                        rs.getDouble("rank")
                ));
            }
        }
        return results;
    }
}
