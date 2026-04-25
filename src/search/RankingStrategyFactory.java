package search;

public class RankingStrategyFactory {
    public static RankingStrategy create(String name, String tsQuery) {
        return switch (name.toLowerCase()) {
            case "alphabetical" -> new AlphabeticalRankingStrategy();
            case "date"         -> new DateRankingStrategy();
            case "path"         -> new PathScoreRankingStrategy();
            default             -> new RelevanceRankingStrategy(tsQuery);
        };
    }
}
