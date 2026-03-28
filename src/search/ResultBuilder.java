package search;

import model.SearchResult;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultBuilder {
    public List<SearchResult> build(ResultSet rs) throws SQLException {
        List<SearchResult> results = new ArrayList<>();
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
        return results;
    }
}
