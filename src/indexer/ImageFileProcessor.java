package indexer;

import model.FileMetadata;
import model.FileRecord;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class ImageFileProcessor implements FileProcessor {
    private static final List<String> IMAGE_EXTENSIONS = List.of(".jpg", ".jpeg", ".png", ".bmp", ".gif");
    private final PathScorer pathScorer;

    public ImageFileProcessor(PathScorer pathScorer) {
        this.pathScorer = pathScorer;
    }

    @Override
    public boolean supports(File file) {
        String name = file.getName().toLowerCase();
        return IMAGE_EXTENSIONS.stream().anyMatch(name::endsWith);
    }

    @Override
    public FileRecord process(File file, FileMetadata metadata, String checksum) throws Exception {
        String dominantColor = extractDominantColor(file);
        double pathScore = pathScorer.score(file);
        return new FileRecord(
                metadata.getAbsolutePath(), metadata.getName(), metadata.getExtension(),
                metadata.getSize(), metadata.getLastModified(), checksum, "", "", pathScore, dominantColor
        );
    }

    private String extractDominantColor(File file) throws Exception {
        BufferedImage image = ImageIO.read(file);
        if (image == null) return null;

        int width = image.getWidth();
        int height = image.getHeight();
        long red = 0, green = 0, blue = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = image.getRGB(x, y);
                red += (rgb >> 16) & 0xFF;
                green += (rgb >> 8) & 0xFF;
                blue += rgb & 0xFF;
            }
        }

        return classifyColor((int) red, (int) green, (int) blue);
    }

    private String classifyColor(int red, int green, int blue) {
        int max = Math.max(red, Math.max(green, blue));
        int min = Math.min(red, Math.min(green, blue));

        /// If the range of color channels is small, the R, G, B values are close together,
        /// which means the color is achromatic: black, white, or gray.
        /// We distinguish between them by brightness.
        if (max - min < 40) {
            if (max < 80) return "black";
            if (min > 180) return "white";
            return "gray";
        }

        /// Find the dominant channel
        if (red == max && red > green + 40 && red > blue + 40) return "red";
        if (green == max && green > red + 40 && green > blue + 40) return "green";
        if (blue == max && blue > red + 40 && blue > green + 40) return "blue";
        if (red > 180 && green > 180 && blue < 100) return "yellow";
        if (red > 180 && green > 100 && blue < 80) return "orange";
        if (red > 150 && blue > 150 && green < 100) return "purple";
        if (red > 180 && green < 100 && blue > 150) return "pink";

        return "mixed";
    }
}