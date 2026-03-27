package search;

public class SearchResult {
    private final String name;
    private final String absolutePath;
    private final String extension;
    private final long size;
    private final String contentPreview;
    private final double rank;

    public SearchResult(String name, String absolutePath, String extension, long size, String contentPreview, double rank) {
        this.name = name;
        this.absolutePath = absolutePath;
        this.extension = extension;
        this.size = size;
        this.contentPreview = contentPreview;
        this.rank = rank;
    }

    public String getName()           { return name; }
    public String getAbsolutePath()   { return absolutePath; }
    public String getExtension()      { return extension; }
    public long   getSize()           { return size; }
    public String getContentPreview() { return contentPreview; }
    public double getRank()           { return rank; }

    @Override
    public String toString() {
        return String.format("[%.4f] %s\n  Path: %s\n  Preview: %s",
                rank, name, absolutePath, contentPreview);
    }
}
