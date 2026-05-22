package indexer;
import model.FileMetadata;
import model.FileRecord;
import java.io.File;

public interface FileProcessor {
    boolean supports(File file);
    FileRecord process(File file, FileMetadata fileMetadata, String checksum) throws Exception;
}
