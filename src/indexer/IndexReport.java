package indexer;

import java.util.ArrayList;
import java.util.List;

public class IndexReport {
    private int totalFound;
    private int totalIndexed;
    private int totalSkipped;
    private int totalFailed;
    private final List<String> failedFiles;
    private int totalPermissionDenied;
    private int totalSymlinksSkipped;
    private int totalUnchanged;
    private int totalUpdated;
    private int totalNew;
    private int totalDeleted;

    public IndexReport() {
        this.failedFiles = new ArrayList<>();
    }

    public void fileFound() { totalFound++; }
    public void fileIndexed() { totalIndexed++; }
    public void fileSkipped() { totalSkipped++; }
    public void fileFailed(String absolutePath) { totalFailed++; failedFiles.add(absolutePath); }
    public void permissionsDenied(String path) {
        totalPermissionDenied++;
        fileSkipped();
    }
    public void symlinksSkipped(String path) {
        totalSymlinksSkipped++;
        fileSkipped();
    }
    public void fileUnchanged() { totalUnchanged++; }
    public void fileUpdated()   { totalUpdated++; }
    public void fileNew()       { totalNew++; }
    public void fileDeleted(int count) { totalDeleted += count; }

    public void print() {
        System.out.println("\n========== INDEXER REPORT ==========");
        System.out.println("Files found:       " + totalFound);
        System.out.println("Files indexed:     " + totalIndexed);
        System.out.println("Files skipped:     " + totalSkipped);
        System.out.println("Files failed:      " + totalFailed);
        System.out.println("Permission denied: " + totalPermissionDenied);
        System.out.println("Symlinks skipped:  " + totalSymlinksSkipped);
        System.out.println("New files:         " + totalNew);
        System.out.println("Updated files:     " + totalUpdated);
        System.out.println("Unchanged files:   " + totalUnchanged);
        System.out.println("Deleted files:     " + totalDeleted);

        if (!failedFiles.isEmpty()) {
            System.out.println("\nFailed files: ");
            failedFiles.forEach(f -> System.out.println("  - " + f));
        }
        System.out.println("==================================\n");
    }

    public int getTotalFound() { return totalFound; }
    public int getTotalIndexed() { return totalIndexed; }
    public int getTotalSkipped() { return totalSkipped; }
    public int getTotalFailed() { return totalFailed; }
}
