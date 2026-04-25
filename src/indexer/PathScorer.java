package indexer;

import java.io.File;
import java.util.List;

public class PathScorer {
    private static final List<String> IMPORTANT_DIRECTORIES = List.of("src", "main", "docs", "lib", "core");
    private static final List<String> IMPORTANT_EXTENSIONS = List.of(".java", ".md", ".txt", ".py", ".js");
    private static final List<String> UNIMPORTANT_EXTENSIONS = List.of(".xml", ".log", ".iml", ".svg");

    public double score(File file){
        double score = 1.0;
        String[] parts = file.getAbsolutePath().toLowerCase().split("[/\\\\]");

        // shorter paths rank higher
        score -= parts.length * 0.05;

        // increase score for important directories in the path
        for(String part : parts){
            if(IMPORTANT_DIRECTORIES.contains(part)){
                score += 0.2;
            }
        }

        // increase/decrease score depending on extension importance
        String name = file.getName().toLowerCase();
        for(String extension : IMPORTANT_EXTENSIONS){
            if(name.endsWith(extension)){
                score += 0.3;
                break;
            }
        }
        for(String extension : UNIMPORTANT_EXTENSIONS){
            if(name.endsWith(extension)){
                score -= 0.3;
                break;
            }
        }

        // increase score for recently modified files (under 30 days)
        long thirtyDaysAgo = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000);
        if(file.lastModified() > thirtyDaysAgo){
            score += 0.2;
        }

        // decrease score of very large files (over 1MB) and very small files (under 10 bytes)
        long size = file.length();
        if(size > 1_000_000) score -= 0.2;
        if(size < 10) score -= 0.1;

        return Math.max(score, 0.0);
    }
}
