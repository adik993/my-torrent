package com.adik993.mytorrent.notification.contexts;


import com.adik993.mytorrent.providers.TorrentProvider;
import reactor.bus.EventBus;

import static com.adik993.mytorrent.notification.Topic.PROVIDER_SEARCH_FAILED;

public class SearchContext extends EventContext<TorrentProvider> {
    public SearchContext(EventBus eventBus) {
        super(eventBus);
    }

    public void notifySearchFailed(TorrentProvider torrentProvider) {
        notifyBus(PROVIDER_SEARCH_FAILED, torrentProvider);
    }
}
