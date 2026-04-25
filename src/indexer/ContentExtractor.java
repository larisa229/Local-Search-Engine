package indexer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ContentExtractor {
    private static final int PREVIEW_LINES = 3;
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
    private static final int MAX_CONTENT_CHARS = 500_000;

    public String extractPreview(File file) {
        if(file.length() > MAX_FILE_SIZE) {
            return "";
        }

        try (Stream<String> lines = Files.lines(file.toPath(), StandardCharsets.UTF_8)) {

            return lines.limit(PREVIEW_LINES)
                    .collect(Collectors.joining("\n"));

        } catch (IOException | UncheckedIOException e) {
            System.err.println("Could not read file: " + file.getAbsolutePath() +
                    " — " + e.getMessage());
            return "";
        } catch(Exception e) {
            System.err.println("Unexpected error reading file: " + file.getAbsolutePath());
            return "";
        }
    }

    public String extractContent(File file) {
        if (file.length() > MAX_FILE_SIZE) return "";
        try (Stream<String> lines = Files.lines(file.toPath(), StandardCharsets.UTF_8)) {
            String content = lines.collect(Collectors.joining("\n"));
            return content.length() > MAX_CONTENT_CHARS ? content.substring(0, MAX_CONTENT_CHARS) : content;
        } catch (Exception e) {
            return "";
        }
    }
}