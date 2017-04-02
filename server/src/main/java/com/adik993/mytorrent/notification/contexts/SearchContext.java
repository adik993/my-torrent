package com.adik993.mytorrent.notification.contexts;


import com.adik993.mytorrent.providers.TorrentsProvider;
import reactor.bus.EventBus;

public class SearchContext extends EventContext<TorrentsProvider> {
    public SearchContext(EventBus eventBus, String topic) {
        super(eventBus, topic);
    }

    public void notifySearchFailed(TorrentsProvider torrentsProvider) {
        notifyBus(torrentsProvider);
    }
}
