package search;

import database.DatabaseConnection;
import model.SearchResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class QueryExecutor {
    private final DatabaseConnection dbConnection;
    private final ResultBuilder resultBuilder;

    private static final String SEARCH_SQL = """
        SELECT name, absolute_path, extension, size, content_preview,
               ts_rank(doc, query) AS rank
        FROM (
            SELECT *, 
                   to_tsvector('english', replace(name, '.', ' ') || ' ' || COALESCE(content_preview, '')) AS doc,
                   to_tsquery('english', ?) AS query
            FROM files
        ) AS subquery
        WHERE doc @@ query
        ORDER BY rank DESC
        """;

    public QueryExecutor(DatabaseConnection dbConnection, ResultBuilder resultBuilder) {
        this.dbConnection = dbConnection;
        this.resultBuilder = resultBuilder;
    }

    public List<SearchResult> execute(String parsedQuery) throws SQLException {
        Connection conn = dbConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(SEARCH_SQL)) {
            stmt.setString(1, parsedQuery);
            ResultSet rs = stmt.executeQuery();
            return resultBuilder.build(rs);
        }
    }
}
