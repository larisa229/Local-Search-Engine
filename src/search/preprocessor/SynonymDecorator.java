package search.preprocessor;

import java.util.Map;

public class SynonymDecorator extends PreProcessorDecorator {
    private static final Map<String, String> SYNONYMS = Map.of(
            "img", "image",
            "pic", "picture",
            "doc", "document",
            "dir", "directory",
            "config", "configuration"
    );

    public SynonymDecorator(QueryPreProcessor queryPreProcessor) {
        super(queryPreProcessor);
    }

    @Override
    public String process(String query) {
        String processed = preProcessor.process(query);
        for(Map.Entry<String, String> entry : SYNONYMS.entrySet()){
            processed = processed.replaceAll("(?i)\\b" + entry.getKey() + "\\b",
                    entry.getKey() + " " + entry.getValue());
        }
        return processed;
    }
}
