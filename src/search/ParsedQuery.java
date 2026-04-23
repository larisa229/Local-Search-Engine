package search;

import java.util.ArrayList;
import java.util.List;

public class ParsedQuery {
    private final List<String> pathTerms = new ArrayList<>();
    private final List<String> contentTerms = new ArrayList<>();
    private final List<String> globalTerms = new ArrayList<>();

    public void addPathTerm(String term) { pathTerms.add(term); }
    public void addContentTerm(String term) { contentTerms.add(term); }
    public void addGlobalTerm(String term) { globalTerms.add(term); }

    public List<String> getPathTerms() { return pathTerms; }
    public List<String> getContentTerms() { return contentTerms; }
    public List<String> getGlobalTerms() { return globalTerms; }
}
