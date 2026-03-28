import config.Config;
import database.DatabaseConnection;
import indexer.*;
import model.FileMetadata;
import model.FileRecord;
import search.*;

import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String configPath = args.length > 0 ? args[0] : "config.properties";
        Config config = new Config(configPath);

        IFileFilter filter = new FileFilter(config.getIgnoreExtensions(), config.getIgnoreDirectories());
        Crawler crawler = new Crawler(filter);
        MetadataExtractor metadataExtractor = new MetadataExtractor();
        ContentExtractor contentExtractor = new ContentExtractor();
        ChecksumCalculator checksumCalculator = new ChecksumCalculator();
        DatabaseConnection dbConnection = new DatabaseConnection(config);
        IndexWriter indexWriter = new IndexWriter(dbConnection);

        SearchController searchController = new SearchController(
                new QueryParser(),
                new QueryExecutor(dbConnection, new ResultBuilder())
        );

        File rootDir = new File(config.getRootDirectory());

        IndexReport report = new IndexReport();
        ChangeDetector changeDetector = new ChangeDetector(dbConnection);
        List<String> processedPaths = new ArrayList<>();

        crawler.crawl(rootDir, file -> {
            report.fileFound();
            System.out.println("Processing: " + file.getName());

            FileMetadata metadata = metadataExtractor.extractFileMetadata(file);
            String checksum = checksumCalculator.calculate(file);

            if (checksum == null) {
                try {
                    new FileInputStream(file).close();
                    report.fileSkipped();
                } catch (Exception ex) {
                    System.err.println("Permission denied: " + file.getName());
                    report.permissionsDenied(file.getAbsolutePath());
                }
                return;
            }

            processedPaths.add(metadata.getAbsolutePath());

            try {
                ChangeDetector.FileStatus status = changeDetector.getStatus(
                        metadata.getAbsolutePath(), checksum
                );

                if(status == ChangeDetector.FileStatus.UNCHANGED) {
                    System.out.println("Unchanged, skipping: " + file.getName());
                    report.fileUnchanged();
                    return;
                }

                String preview = contentExtractor.extractPreview(file);
                FileRecord record = new FileRecord(
                        metadata.getAbsolutePath(),
                        metadata.getName(),
                        metadata.getExtension(),
                        metadata.getSize(),
                        metadata.getLastModified(),
                        checksum,
                        preview
                );

                indexWriter.write(record);
                report.fileIndexed();

                if (status == ChangeDetector.FileStatus.NEW) {
                    System.out.println("New file indexed: " + file.getName());
                    report.fileNew();
                } else {
                    System.out.println("Updated file: " + file.getName());
                    report.fileUpdated();
                }
            } catch (SQLException e) {
                System.err.println("Failed to index: " + metadata.getAbsolutePath() + " — " + e.getMessage());
                report.fileFailed(metadata.getAbsolutePath());
            }

        }, report);

        try {
            int deleted = changeDetector.removeDeletedFiles(processedPaths);
            report.fileDeleted(deleted);
        } catch (SQLException e) {
            System.err.println("Failed to remove deleted files: " + e.getMessage());
        }

        report.print();

        System.out.println("Single word search: 'crawler'");
        searchController.search("crawler").forEach(System.out::println);

        System.out.println("Multi word search: 'public class'");
        searchController.search("public class").forEach(System.out::println);

        try {
            dbConnection.close();
        } catch (SQLException e) {
            System.err.println("Failed to close DB connection: " + e.getMessage());
        }
    }
}