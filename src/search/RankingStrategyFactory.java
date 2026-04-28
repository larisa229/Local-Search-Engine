package search;

public class RankingStrategyFactory {
    public static RankingStrategy create(String name, String tsQuery, SearchHistory searchHistory) {
        return switch (name.toLowerCase()) {
            case "alphabetical" -> new AlphabeticalRankingStrategy();
            case "date"         -> new DateRankingStrategy();
            case "path"         -> new PathScoreRankingStrategy();
            case "history"      -> new HistoryRankingStrategy(searchHistory);
            default             -> new RelevanceRankingStrategy(tsQuery);
        };
    }
}
