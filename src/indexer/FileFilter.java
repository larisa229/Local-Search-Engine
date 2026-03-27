package indexer;

import java.io.File;
import java.util.List;

public class FileFilter implements IFileFilter {
    private final List<String> ignoredExtensions;
    private final List<String> ignoredDirectories;

    public FileFilter(List<String> ignoredExtensions, List<String> ignoredDirectories) {
        this.ignoredExtensions = ignoredExtensions.stream()
                .map(e -> e.startsWith(".") ? e : "." + e)
                .toList();
        this.ignoredDirectories = ignoredDirectories;
    }

    @Override
    public boolean isAccepted(File file) {
        if(file.isDirectory()) {
            return !ignoredDirectories.contains(file.getName());
        }

        String fileName = file.getName();
        return ignoredExtensions.stream().noneMatch(fileName::endsWith);
    }
}
