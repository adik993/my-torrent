package com.adik993.mytorrent.clients;


import com.adik993.tpbclient.model.Torrent;
import io.reactivex.Single;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class TorrentClient {
    @Getter
    final String id = UUID.randomUUID().toString();
    final AtomicBoolean up = new AtomicBoolean(true);

    public abstract String getName();

    public abstract List<String> getCategories();

    boolean supportsCategory(String category) {
        return getCategories().contains(category);
    }

    public abstract Single<Page<Torrent>> search(String query, String category, Integer page, String orderBy);

    public abstract Single<TorrentClient> checkIfUp();

    boolean isUp() {
        return up.get();
    }

    @Override
    public String toString() {
        return "TorrentClient(" +
                "id=" + id +
                ", name=" + getName() +
                ')';
    }
}
