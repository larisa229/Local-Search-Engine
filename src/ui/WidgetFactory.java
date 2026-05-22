package ui;

import model.SearchResult;

import java.util.List;
import java.util.stream.Collectors;

public class WidgetFactory {
    private static final List<Widget> ALL_WIDGETS = List.of(
            new GalleryWidget(),
            new CodeAnalyzerWidget()
    );

    public static List<Widget> getActiveWidgets(List<SearchResult> results) {
        return ALL_WIDGETS.stream()
                .filter(widget -> widget.shouldActivate(results))
                .collect(Collectors.toList());
    }
}

