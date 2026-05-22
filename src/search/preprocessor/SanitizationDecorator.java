package search.preprocessor;

public class SanitizationDecorator extends PreProcessorDecorator {
    public SanitizationDecorator(QueryPreProcessor queryPreProcessor) {
        super(queryPreProcessor);
    }

    @Override
    public String process(String query) {
        String processed = preProcessor.process(query);
        return processed.replaceAll("[^a-zA-Z0-9 :_\\-]", " ").trim();
    }
}
