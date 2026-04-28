package search;

import java.util.List;

public class HistoryRankingStrategy implements RankingStrategy {
    private final SearchHistory searchHistory;

    public HistoryRankingStrategy(SearchHistory searchHistory) {
        this.searchHistory = searchHistory;
    }

    @Override
    public String getSelectExpression() {
        // increase score based on how often paths matching top queries were accessed
        List<String> topQueries = searchHistory.getTopQueries(5);
        if(topQueries.isEmpty()) return "path_score";

        StringBuilder expr = new StringBuilder("(path_score");
        for(String query : topQueries) {
            expr.append(" + CASE WHEN absolute_path ILIKE '%")
                    .append(query.replace("'", "''"))
                    .append("%' THEN 0.1 ELSE 0 END");
        }
        expr.append(")");
        return expr.toString();
    }

    @Override
    public String getOrderByExpression() {
        return "rank DESC";
    }

}
