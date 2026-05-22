package ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.SearchResult;

import java.util.List;

public class GalleryWidget implements Widget {
    private static final List<String> IMAGE_EXTENSIONS = List.of(".jpg", ".jpeg", ".png", ".bmp", ".gif");
    private static final double THRESHOLD = 0.4;

    @Override
    public boolean shouldActivate(List<SearchResult> results) {
        if(results.isEmpty()) return false;
        long imageCount = results.stream()
                .filter(result -> IMAGE_EXTENSIONS.contains(result.getExtension().toLowerCase()))
                .count();
        return (double) imageCount / results.size() > THRESHOLD;
    }

    @Override
    public String getLabel() {
        return "View as Gallery";
    }

    @Override
    public void activate(List<SearchResult> results) {
        Stage galleryStage = new Stage();
        galleryStage.setTitle("Image Gallery");

        FlowPane galleryPane = new FlowPane(10, 10);
        galleryPane.setPadding(new Insets(20));
        galleryPane.setPrefWrapLength(700);

        results.stream()
                .filter(result -> IMAGE_EXTENSIONS.contains(result.getExtension().toLowerCase()))
                .forEach(result -> {
                    try {
                        // Image loads from a URL, not file path, so convert the path
                        ImageView imageView = new ImageView(
                                new Image("file:///" + result.getAbsolutePath().replace("\\", "/"))
                        );
                        imageView.setFitWidth(100);
                        imageView.setFitHeight(100);
                        imageView.setPreserveRatio(true);

                        Label nameLabel = new Label(result.getName());
                        nameLabel.setMaxWidth(100);

                        VBox imageBox = new VBox(5, imageView, nameLabel);
                        galleryPane.getChildren().add(imageBox);
                    } catch (Exception e) {
                        System.err.println("Could not load image: " + result.getAbsolutePath());
                    }
                });

        galleryStage.setScene(new Scene(new ScrollPane(galleryPane), 750, 500));
        galleryStage.show();
    }
}
