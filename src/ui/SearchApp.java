package ui;

import config.Config;
import database.DatabaseConnection;
import indexer.ChangeDetector;
import indexer.ChecksumCalculator;
import indexer.ContentExtractor;
import indexer.Crawler;
import model.FileMetadata;
import indexer.FileProcessor;
import indexer.FileProcessorFactory;
import indexer.IFileFilter;
import indexer.FileFilter;
import indexer.ImageFileProcessor;
import indexer.IndexReport;
import indexer.IndexWriter;
import indexer.MetadataExtractor;
import indexer.PathScorer;
import indexer.TextFileProcessor;
import javafx.application.Application;
import javafx.stage.Stage;
import model.FileRecord;
import search.QueryExecutor;
import search.QueryParser;
import search.ResultBuilder;
import search.SearchController;
import search.SearchHistory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SearchApp extends Application {
    private SearchController searchController;
    private SearchHistory searchHistory;

    @Override
    public void start(Stage primaryStage) {
        String configPath = "config.properties";
        Config config = new Config(configPath);

        IFileFilter filter = new FileFilter(config.getIgnoreExtensions(), config.getIgnoreDirectories());
        Crawler crawler = new Crawler(filter);
        ChecksumCalculator checksumCalculator = new ChecksumCalculator();
        MetadataExtractor metadataExtractor = new MetadataExtractor();
        DatabaseConnection dbConnection = new DatabaseConnection(config);
        IndexWriter indexWriter = new IndexWriter(dbConnection);

        FileProcessorFactory processorFactory = new FileProcessorFactory(List.of(
                new TextFileProcessor(new ContentExtractor(), new PathScorer()),
                new ImageFileProcessor(new PathScorer())
        ));

        searchHistory = new SearchHistory();
        searchController = new SearchController(
                new QueryParser(),
                new QueryExecutor(dbConnection, new ResultBuilder()),
                searchHistory
        );
        searchController.addObserver(searchHistory);

        // run indexing in background so UI doesn't freeze
        new Thread(() -> {
            File rootDir = new File(config.getRootDirectory());
            IndexReport report = new IndexReport();
            ChangeDetector changeDetector = new ChangeDetector(dbConnection);
            List<String> processedPaths = new ArrayList<>();

            List<File> files = crawler.crawl(rootDir);
            for (File file : files) {
                report.fileFound();
                FileMetadata metadata = metadataExtractor.extractFileMetadata(file);
                String checksum = checksumCalculator.calculate(file);
                if (checksum == null) { report.fileSkipped(); continue; }
                processedPaths.add(metadata.getAbsolutePath());

                try {
                    ChangeDetector.FileStatus status = changeDetector.getStatus(metadata.getAbsolutePath(), checksum);
                    if (status == ChangeDetector.FileStatus.UNCHANGED) { report.fileUnchanged(); continue; }

                    Optional<FileProcessor> processor = processorFactory.getFileProcessor(file);
                    if(processor.isEmpty()) {
                        report.fileSkipped();
                        continue;
                    }

                    FileRecord record = processor.get().process(file, metadata, checksum);
                    indexWriter.write(record);
                    report.fileIndexed();

                    if (status == ChangeDetector.FileStatus.NEW) { report.fileNew(); }
                    else { report.fileUpdated(); }

                } catch (Exception e) {
                    System.err.println("Failed to index: " + e.getMessage());
                    report.fileFailed(file.getAbsolutePath());
                }
            }

            try { changeDetector.removeDeletedFiles(processedPaths); } catch (Exception e) { System.err.println(e.getMessage()); }
            report.print();
        }).start();

        SearchView view = new SearchView(searchController, searchHistory);
        primaryStage.setScene(view.buildScene());
        primaryStage.setTitle("Search Engine");
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.show();
    }
}