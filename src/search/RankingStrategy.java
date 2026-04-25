package search;

public interface RankingStrategy {
    String getSelectExpression();  // what to put in the SELECT as rank
    String getOrderByExpression(); // what to put in ORDER BY
}
