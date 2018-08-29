package com.adik993.mytorrent.clients;

import lombok.Value;

@Value
public class TorrentClientDto {
    String id;
    String name;
    boolean up;

    public TorrentClientDto(TorrentClient provider) {
        this.id = provider.getId();
        this.name = provider.getName();
        this.up = provider.isUp();
    }
}
