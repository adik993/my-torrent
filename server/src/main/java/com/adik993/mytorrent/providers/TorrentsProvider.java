package com.adik993.mytorrent.providers;


import com.adik993.tpbclient.exceptions.ParseException;
import com.adik993.tpbclient.model.Torrent;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class TorrentsProvider {
    @Getter
    @Setter
    Long id;
    final AtomicBoolean up = new AtomicBoolean(true);

    public abstract String getName();

    public abstract List<String> getCategories();

    boolean supportsCategory(String category) {
        return getCategories().contains(category);
    }

    public abstract Page<Torrent> search(String query, String category, Integer page, String orderBy) throws IOException, ParseException;

    public abstract boolean checkIfUp();

    boolean isUp() {
        return up.get();
    }

    @Override
    public String toString() {
        return "TorrentsProvider(" +
                "id=" + id +
                ", name=" + getName() +
                ')';
    }
}
