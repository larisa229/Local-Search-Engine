package search;

public class PathScoreRankingStrategy implements RankingStrategy {
    @Override
    public String getSelectExpression() {
        return "path_score";
    }

    @Override
    public String getOrderByExpression() {
        return "rank DESC";
    }
}
