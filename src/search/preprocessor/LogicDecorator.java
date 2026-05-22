package search.preprocessor;

public class LogicDecorator extends PreProcessorDecorator {
    public LogicDecorator(QueryPreProcessor queryPreProcessor) {
        super(queryPreProcessor);
    }

    @Override
    public String process(String query) {
        String processed = preProcessor.process(query);
        String[] tokens = processed.trim().split("\\s+");
        StringBuilder result = new StringBuilder();
        for (String token : tokens) {
            result.append(token);
            if(!token.contains(":") && !token.endsWith("*")) {
                result.append(" ").append(token).append("*");
            }
            result.append(" ");
        }
        return result.toString().trim();
    }
}
