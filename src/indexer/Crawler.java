package indexer;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Crawler {
    private final IFileFilter filter;

    public Crawler(IFileFilter filter) {
        this.filter = filter;
    }

    public List<File> crawl(File rootDir) {
        List<File> result = new ArrayList<>();

        if(!rootDir.exists() || !rootDir.isDirectory()) {
            System.err.println("Root directory does not exist or is not a directory: " + rootDir.getAbsolutePath());
            return result;
        }

        if(!rootDir.canRead()) {
            System.err.println("Permission denied, cannot read root directory: " + rootDir.getAbsolutePath());
            return result;
        }

        File[] files = rootDir.listFiles();
        if(files == null) {
            System.err.println("Cannot access directory: " + rootDir.getAbsolutePath());
            return result;
        }

        for(File file : files) {
            if(!filter.isAccepted(file)) continue;

            if(file.isDirectory()) {
                if(Files.isSymbolicLink(file.toPath())) {
                    System.out.println("Skipping symlink to avoid loop: " + file.getAbsolutePath());
                    continue;
                }
                result.addAll(crawl(file));
            } else {
                if(!file.canRead()) {
                    System.err.println("Permission denied, cannot read file: " + file.getAbsolutePath());
                    continue;
                }
                result.add(file);
            }
        }
        return result;
    }
}
