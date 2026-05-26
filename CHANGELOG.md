# Changelog

## [3.0.0] - 2026-05-26
### Added
- Multimodal search with dominant color extraction from images
- Strategy pattern for file type handling during indexing (TextFileProcessor, ImageFileProcessor)
- Context-aware widgets: Gallery and Code Analyzer activated based on result set composition
- Query pre-processor pipeline via Decorator pattern (SanitizationDecorator, SynonymDecorator, LogicDecorator)
- Checkstyle linting with pre-commit hook
- Git tags and CHANGELOG

## [2.0.0] - 2026-04-26
### Added
- Full-text search with persistent tsvector and GIN index
- Full file content storage for deeper search
- Path scoring at index time
- Swappable ranking strategies: relevance, alphabetical, date, path, history
- Search history tracking via Observer pattern
- Query suggestions based on search history
- JavaFX GUI with live suggestions and strategy dropdown

## [1.0.0] - 2026-03-27
### Added
- File system crawler
- Metadata and content extraction
- SHA-256 checksum-based change detection
- PostgreSQL indexing with upsert
- Basic full-text search
- Index report with statistics
