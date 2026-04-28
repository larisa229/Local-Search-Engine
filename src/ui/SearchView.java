package ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.SearchResult;
import search.SearchController;
import search.SearchHistory;

import java.util.List;

public class SearchView {
    private final SearchController searchController;
    private final SearchHistory searchHistory;

    public SearchView(SearchController searchController, SearchHistory searchHistory) {
        this.searchController = searchController;
        this.searchHistory = searchHistory;
    }

    public Scene buildScene() {
        Label title = new Label("Search Engine");
        title.setFont(Font.font("Monospace", FontWeight.BOLD, 20));

        TextField searchField = new TextField();
        searchField.setPromptText("Search... (e.g. path:src content:main)");
        searchField.setPrefWidth(600);

        ComboBox<String> strategyBox = new ComboBox<>(
                FXCollections.observableArrayList("relevance", "alphabetical", "date", "path", "history")
        );
        strategyBox.setValue("relevance");
        strategyBox.setOnAction(e -> searchController.setStrategy(strategyBox.getValue()));

        Button searchButton = new Button("Search");

        HBox searchBar = new HBox(10, searchField, strategyBox, searchButton);
        searchBar.setAlignment(Pos.CENTER);

        ListView<String> suggestions = new ListView<>();
        suggestions.setPrefHeight(100);
        suggestions.setVisible(false);

        ListView<String> results = new ListView<>();
        VBox.setVgrow(results, Priority.ALWAYS);

        Label statusLabel = new Label("");

        VBox root = new VBox(12, title, searchBar, suggestions, statusLabel, results);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isBlank()) {
                suggestions.setVisible(false);
                return;
            }
            List<String> hits = searchHistory.suggest(newVal);
            if (hits.isEmpty()) {
                suggestions.setVisible(false);
            } else {
                suggestions.setItems(FXCollections.observableArrayList(hits));
                suggestions.setVisible(true);
            }
        });

        suggestions.setOnMouseClicked(e -> {
            String selected = suggestions.getSelectionModel().getSelectedItem();
            if (selected != null) {
                searchField.setText(selected);
                suggestions.setVisible(false);
            }
        });

        searchButton.setOnAction(e -> {
            String query = searchField.getText().trim();
            if (query.isBlank()) return;
            suggestions.setVisible(false);

            new Thread(() -> {
                List<SearchResult> searchResults = searchController.search(query);
                Platform.runLater(() -> {
                    if (searchResults.isEmpty()) {
                        statusLabel.setText("No results found.");
                        results.setItems(FXCollections.observableArrayList());
                    } else {
                        statusLabel.setText(searchResults.size() + " result(s) found.");
                        results.setItems(FXCollections.observableArrayList(
                                searchResults.stream().map(SearchResult::toString).toList()
                        ));
                    }
                });
            }).start();
        });

        searchField.setOnAction(e -> searchButton.fire());

        return new Scene(root, 800, 600);
    }
}