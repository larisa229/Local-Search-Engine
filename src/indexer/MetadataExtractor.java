import java.io.File;

public class MetadataExtractor {
    public FileMetadata extractFileMetadata(File file) {
        String name = file.getName();
        String extension = extractExtension(name);
        long size = file.length();
        long lastModified = file.lastModified();
        String path = file.getPath();

        return new  FileMetadata(name, extension, size, lastModified, path);
    }

    private String extractExtension(String name) {
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == name.length() - 1) {
            return "";
        }
        return name.substring(dotIndex);
    }
}
