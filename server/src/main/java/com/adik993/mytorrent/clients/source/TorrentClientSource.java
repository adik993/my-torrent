package com.adik993.mytorrent.clients.source;

import com.adik993.mytorrent.clients.TorrentClient;
import io.reactivex.Flowable;

public interface TorrentClientSource {
    Flowable<TorrentClient> provide();
}
