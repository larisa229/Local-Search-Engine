package search;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchHistory implements SearchObserver {
    private final Map<String, Integer> queryCounts = new HashMap<>();

    @Override
    public void onSearch(String query) {
        queryCounts.put(query, queryCounts.getOrDefault(query, 0) + 1);
    }

    // get queries that start with the given prefix and sort by frequency
    public List<String> suggest(String prefix) {
        return queryCounts.entrySet().stream()
                .filter(e -> e.getKey().startsWith(prefix))
                .sorted((a, b) -> b.getValue() - a.getValue())
                .map(Map.Entry::getKey)
                .toList();
    }

    // get the top n most searched queries
    public List<String> getTopQueries(int n) {
        return queryCounts.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(n)
                .map(Map.Entry::getKey)
                .toList();
    }
}
