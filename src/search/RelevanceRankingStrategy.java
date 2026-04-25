package search;

public class RelevanceRankingStrategy implements RankingStrategy {
    private final String tsQuery;

    public RelevanceRankingStrategy(String tsQuery) {
        this.tsQuery = tsQuery;
    }

    @Override
    public String getSelectExpression() {
        return "(path_score + ts_rank(search_vector, plainto_tsquery('english', ?)))";
    }

    @Override
    public String getOrderByExpression() {
        return "rank DESC";
    }

    public String getTsQuery() {
        return tsQuery;
    }
}
