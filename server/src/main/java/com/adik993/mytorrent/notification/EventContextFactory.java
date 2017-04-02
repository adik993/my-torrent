package com.adik993.mytorrent.notification;


import com.adik993.mytorrent.notification.contexts.ProviderUpdateContext;
import com.adik993.mytorrent.notification.contexts.SearchContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.bus.EventBus;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventContextFactory {
    private final EventBus eventBus;

    public ProviderUpdateContext createProviderUpdateContext() {
        return new ProviderUpdateContext(eventBus, Topic.PROVIDER_STATUS_UPDATE_DONE);
    }

    public SearchContext createSearchContext() {
        return new SearchContext(eventBus, Topic.PROVIDER_SEARCH_FAILED);
    }
}
