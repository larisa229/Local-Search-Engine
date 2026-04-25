package search;

public class AlphabeticalRankingStrategy implements RankingStrategy {
    @Override
    public String getSelectExpression() {
        return "0";
    }

    @Override
    public String getOrderByExpression() {
        return "name ASC";
    }
}
