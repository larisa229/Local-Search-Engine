package indexer;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class FileProcessorFactory {
    private final List<FileProcessor> fileProcessors;

    public FileProcessorFactory(List<FileProcessor> fileProcessors) {
        this.fileProcessors = fileProcessors;
    }

    public Optional<FileProcessor> getFileProcessor(File file) {
        return fileProcessors.stream().filter(processor -> processor.supports(file)).findFirst();
    }
}
