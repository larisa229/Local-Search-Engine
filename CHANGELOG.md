# Changelog

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
