package com.adik993.mytorrent.notification.contexts;


import com.adik993.mytorrent.clients.TorrentClient;
import com.adik993.mytorrent.notification.EventBus;

import static com.adik993.mytorrent.notification.Topic.UPDATE_CLIENTS;

public class SearchContext extends EventContext<TorrentClient> {
    public SearchContext(EventBus eventBus) {
        super(eventBus);
    }

    public void notifySearchFailed(TorrentClient torrentClient) {
        notifyBus(UPDATE_CLIENTS, torrentClient);
    }
}
