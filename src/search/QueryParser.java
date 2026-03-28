package search;

public class QueryParser {
    public String parse(String query) {
        String trimmed = query.trim().toLowerCase();
        String[] words = trimmed.split("\\s+");
        return String.join(" & ", words);
    }
}
