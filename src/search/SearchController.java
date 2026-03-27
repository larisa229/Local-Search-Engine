import java.beans.beancontext.BeanContextEvent;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class SearchController {
    private final QueryParser parser;
    private final QueryExecutor executor;

    public SearchController(QueryParser parser, QueryExecutor executor) {
        this.parser = parser;
        this.executor = executor;
    }

    public List<SearchResult> search(String query) {
        if(query == null || query.isBlank()) {
            return Collections.emptyList();
        }

        try {
            String parsedQuery = parser.parse(query);
            return executor.execute(parsedQuery);
        } catch (SQLException e) {
            System.err.println("Search failed: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
