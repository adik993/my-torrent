package com.adik993.mytorrent.notification;


import com.adik993.mytorrent.notification.contexts.SearchContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EventContextFactory {
    private final EventBus eventBus;

    public SearchContext createSearchContext() {
        return new SearchContext(eventBus);
    }
}
