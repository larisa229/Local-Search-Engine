package search.preprocessor;

public abstract class PreProcessorDecorator implements QueryPreProcessor {
    protected final QueryPreProcessor preProcessor;

    public PreProcessorDecorator(QueryPreProcessor preProcessor) {
        this.preProcessor = preProcessor;
    }
}
