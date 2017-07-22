package com.adik993.mytorrent.providers;

import lombok.Value;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Value
public class TorrentProviderDto {
    Long id;
    String name;
    boolean up;

    static TorrentProviderDto from(TorrentProvider tp) {
        return new TorrentProviderDto(tp.getId(), tp.getName(), tp.isUp());
    }

    public static List<TorrentProviderDto> fromCollection(Collection<TorrentProvider> list) {
        return list.stream().map(TorrentProviderDto::from).collect(Collectors.toList());
    }
}
