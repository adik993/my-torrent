package com.adik993.mytorrent.notification.contexts;


import com.adik993.mytorrent.providers.TorrentProvider;
import reactor.bus.EventBus;

public class SearchContext extends EventContext<TorrentProvider> {
    public SearchContext(EventBus eventBus, String topic) {
        super(eventBus, topic);
    }

    public void notifySearchFailed(TorrentProvider torrentProvider) {
        notifyBus(torrentProvider);
    }
}
