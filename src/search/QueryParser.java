package search;

public class QueryParser {

    public ParsedQuery parse(String rawQuery) {
        ParsedQuery parsedQuery = new ParsedQuery();
        if(rawQuery == null || rawQuery.isBlank()) return parsedQuery;

        String[] parts = rawQuery.trim().split("\\s+");

        for (String part : parts) {
            if (part.startsWith("path:")) {
                String value = part.substring(5);
                if(!value.isEmpty()) parsedQuery.addPathTerm(value);
            } else if (part.startsWith("content:")) {
                String value = part.substring(8);
                if(!value.isEmpty()) parsedQuery.addContentTerm(value);
            } else {
                parsedQuery.addGlobalTerm(part);
            }
        }
        return parsedQuery;
    }
}