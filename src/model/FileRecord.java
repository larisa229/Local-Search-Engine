package model;

import indexer.PathScorer;

public class FileRecord {
    private final String absolutePath;
    private final String name;
    private final String extension;
    private final long size;
    private final long lastModified;
    private final String checksum;
    private final String contentPreview;
    private final String content;
    private final double pathScore;

    public FileRecord(String absolutePath, String name, String extension, long size, long lastModified,
                      String checksum, String contentPreview, String content, double pathScore) {
        this.absolutePath = absolutePath;
        this.name = name;
        this.extension = extension;
        this.size = size;
        this.lastModified = lastModified;
        this.checksum = checksum;
        this.contentPreview = contentPreview;
        this.content = content;
        this.pathScore = pathScore;
    }

    public String getAbsolutePath()   { return absolutePath; }
    public String getName()           { return name; }
    public String getExtension()      { return extension; }
    public long   getSize()           { return size; }
    public long   getLastModified()   { return lastModified; }
    public String getChecksum()       { return checksum; }
    public String getContentPreview() { return contentPreview; }
    public String getContent()       { return content; }
    public double getPathScore()     { return pathScore; }
}
