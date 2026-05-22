package search;

import model.SearchResult;
import search.parsing.ParsedQuery;
import search.parsing.QueryParser;
import search.preprocessor.*;
import search.ranking.RankingStrategy;
import search.ranking.RankingStrategyFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchController {
    private final QueryParser parser;
    private final QueryExecutor executor;
    private final SearchHistory searchHistory;
    private final List<SearchObserver> observers = new ArrayList<>();
    private final QueryPreProcessor preProcessor;
    private String currentStrategy = "relevance";

    public SearchController(QueryParser parser, QueryExecutor executor, SearchHistory searchHistory) {
        this.parser = parser;
        this.executor = executor;
        this.searchHistory = searchHistory;

        this.preProcessor = new LogicDecorator(
                new SynonymDecorator(
                        new SanitizationDecorator(
                                new BasePreProcessor()
                        )
                )
        );
    }

    public void addObserver(SearchObserver observer) {
        observers.add(observer);
    }

    public void setStrategy(String strategy) {
        this.currentStrategy = strategy;
    }

    public List<SearchResult> search(String query) {
        if(query == null || query.isBlank()) {
            return Collections.emptyList();
        }

        notifyObservers(query);

        try {
            String processedQuery = preProcessor.process(query);
            System.out.println("Original: " + query);
            System.out.println("Processed: " + processedQuery);
            ParsedQuery parsedQuery = parser.parse(processedQuery);
            String tsQuery = String.join(" ", parsedQuery.getContentTerms());
            RankingStrategy strategy = RankingStrategyFactory.create(currentStrategy, tsQuery, searchHistory);
            return executor.execute(parsedQuery, strategy);
        } catch (SQLException e) {
            System.err.println("Search failed: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private void notifyObservers(String query) {
        for (SearchObserver observer : observers) {
            observer.onSearch(query);
        }
    }
}
