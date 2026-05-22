package search.preprocessor;

public class BasePreProcessor implements QueryPreProcessor {
    @Override
    public String process(String query) {
        return query;
    }
}
