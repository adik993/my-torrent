package com.adik993.mytorrent.providers;

import lombok.Value;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Value
public class TorrentsProviderDto {
    Long id;
    String name;
    boolean up;

    static TorrentsProviderDto from(TorrentsProvider tp) {
        return new TorrentsProviderDto(tp.getId(), tp.getName(), tp.isUp());
    }

    public static List<TorrentsProviderDto> fromCollection(Collection<TorrentsProvider> list) {
        return list.stream().map(TorrentsProviderDto::from).collect(Collectors.toList());
    }
}
