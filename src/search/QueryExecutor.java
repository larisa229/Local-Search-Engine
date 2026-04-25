package search;

import database.DatabaseConnection;
import model.SearchResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueryExecutor {
    private final DatabaseConnection dbConnection;
    private final ResultBuilder resultBuilder;

    public QueryExecutor(DatabaseConnection dbConnection, ResultBuilder resultBuilder) {
        this.dbConnection = dbConnection;
        this.resultBuilder = resultBuilder;
    }

    public List<SearchResult> execute(ParsedQuery parsedQuery, RankingStrategy strategy) throws SQLException {
        List<String> tsTerms = new ArrayList<>(parsedQuery.getContentTerms());
        tsTerms.addAll(parsedQuery.getGlobalTerms());
        boolean hasContentQuery = !tsTerms.isEmpty();
        String joinedTerms = String.join(" ", tsTerms);

        StringBuilder sql = new StringBuilder(
                "SELECT name, absolute_path, extension, size, content_preview, "
        );

       sql.append(strategy.getSelectExpression()).append(" AS rank ");
        sql.append("FROM files WHERE 1=1 ");

        List<String> params = new ArrayList<>();
        if (strategy instanceof RelevanceRankingStrategy rel) {
            params.add(rel.getTsQuery());
        }

        // ILIKE - case-insensitive LIKE
        for (String p : parsedQuery.getPathTerms()) {
            sql.append(" AND absolute_path ILIKE ?");
            params.add("%" + p + "%"); // find any path that has p inside it
        }

        if (hasContentQuery) {
            sql.append(" AND search_vector @@ plainto_tsquery('english', ?)");
            params.add(joinedTerms);
        }

        sql.append(" ORDER BY ").append(strategy.getOrderByExpression());

        Connection conn = dbConnection.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql.toString())) {
            for(int i = 0; i < params.size(); i++){
                preparedStatement.setString(i + 1, params.get(i));
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultBuilder.build(resultSet);
        }
    }
}
