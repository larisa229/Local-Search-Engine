package indexer;

import java.io.File;

public interface IFileFilter {
    boolean isAccepted(File file);
}
