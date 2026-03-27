import java.io.File;
import java.nio.file.Files;
import java.util.function.Consumer;

public class Crawler {
    private final IFileFilter filter;

    public Crawler(IFileFilter filter) {
        this.filter = filter;
    }

    public void crawl(File rootDir, Consumer<File> onFileAccepted) {
        if(!rootDir.exists() || !rootDir.isDirectory()) {
            return;
        }

        File[] files = rootDir.listFiles();
        if(files == null) {
            System.err.println("Cannot access directory: " + rootDir.getAbsolutePath());
            return;
        }

        for(File file : files) {
            if(filter.isAccepted(file)) {
                if(file.isDirectory()) {
                    if(!Files.isSymbolicLink(file.toPath())) {
                        crawl(file, onFileAccepted);
                    }
                } else {
                    onFileAccepted.accept(file);
                }
            }
        }
    }
}
