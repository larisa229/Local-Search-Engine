import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class ContentExtractor {
    private static final int PREVIEW_LINES = 3;
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    public String extractPreview(File file) {
        if(file.length() > MAX_FILE_SIZE) {
            return "";
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            return reader.lines()
                    .limit(PREVIEW_LINES)
                    .collect(Collectors.joining("\n"));

        } catch (IOException e) {
            System.err.println("Could not read file: " + file.getAbsolutePath() +
                    " — " + e.getMessage());
            return "";
        }

    }
}
