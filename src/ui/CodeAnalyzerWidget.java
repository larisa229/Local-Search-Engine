package ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.SearchResult;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.*;

public class CodeAnalyzerWidget implements Widget {
    private static final List<String> CODE_EXTENSIONS = List.of(".java", ".py", ".js", ".html", ".css");
    private static final double THRESHOLD = 0.25;

    @Override
    public boolean shouldActivate(List<SearchResult> results) {
        if(results.isEmpty()) return false;
        long codeCount = results.stream()
                .filter(result -> CODE_EXTENSIONS.contains(result.getExtension().toLowerCase()))
                .count();
        System.out.println("Code files: " + codeCount + " / " + results.size());
        return (double) codeCount / results.size() >= THRESHOLD;
    }

    @Override
    public String getLabel() {
        return "Analyze code";
    }

    @Override
    public void activate(List<SearchResult> results) {
        Stage stage = new Stage();
        stage.setTitle("Code Analysis");

        VBox box = new VBox(10);
        box.setPadding(new Insets(20));

        Map<String, Long> byExtension = results.stream()
                .filter(result -> CODE_EXTENSIONS.contains(result.getExtension().toLowerCase()))
                .collect(Collectors.groupingBy(result -> result.getExtension().toLowerCase(), Collectors.counting()));

        box.getChildren().add(new Label("Code files in results:"));
        byExtension.forEach((extension, count) ->
                box.getChildren().add(new Label("  " + extension + ": " + count + " file(s)"))
        );

        long totalSize = results.stream()
                .filter(result -> CODE_EXTENSIONS.contains(result.getExtension().toLowerCase()))
                .mapToLong(SearchResult::getSize)
                .sum();
        box.getChildren().add(new Label("Total size: " + totalSize / 1024 + " KB"));

        stage.setScene(new Scene(new ScrollPane(box), 400, 300));
        stage.show();
    }
}
