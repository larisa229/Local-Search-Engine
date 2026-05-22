package indexer;
import model.FileMetadata;
import model.FileRecord;
import java.io.File;
import java.util.List;

public class TextFileProcessor implements FileProcessor {
    private static final List<String> TEXT_EXTENSIONS = List.of(
            ".java", ".txt", ".md", ".py", ".js", ".html", ".css", ".xml", ".json", ".properties"
    );

    private final ContentExtractor contentExtractor;
    private final PathScorer pathScorer;

    public TextFileProcessor(ContentExtractor contentExtractor, PathScorer pathScorer) {
        this.contentExtractor = contentExtractor;
        this.pathScorer = pathScorer;
    }

    @Override
    public boolean supports(File file) {
        String name = file.getName().toLowerCase();
        return TEXT_EXTENSIONS.stream().anyMatch(name::endsWith);
    }

    @Override
    public FileRecord process(File file, FileMetadata metadata, String checksum) throws Exception {
        String preview = contentExtractor.extractPreview(file);
        String content = contentExtractor.extractContent(file);
        double pathScore = pathScorer.score(file);
        return new FileRecord(
                metadata.getAbsolutePath(), metadata.getName(), metadata.getExtension(),
                metadata.getSize(), metadata.getLastModified(), checksum, preview, content, pathScore, null
        );
    }
}