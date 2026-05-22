package ui;

import model.SearchResult;

import java.util.List;

public interface Widget {
    boolean shouldActivate(List<SearchResult> results);
    String getLabel();
    void activate(List<SearchResult> results);
}
