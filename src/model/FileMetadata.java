package model;

public class FileMetadata {
    private final String name;
    private final String extension;
    private final long size;
    private final long lastModified;
    private final String absolutePath;

    public FileMetadata(String name, String extension, long size, long lastModified, String absolutePath) {
        this.name = name;
        this.extension = extension;
        this.size = size;
        this.lastModified = lastModified;
        this.absolutePath = absolutePath;
    }

    public String getName() {
        return name;
    }

    public String getExtension() {
        return extension;
    }

    public long getSize() {
        return size;
    }

    public long getLastModified() {
        return lastModified;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }
}
