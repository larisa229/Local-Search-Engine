package search;

public class DateRankingStrategy implements RankingStrategy {
    @Override
    public String getSelectExpression() {
        return "0";
    }

    @Override
    public String getOrderByExpression() {
        return "last_modified DESC";
    }
}
