package com.adik993.mytorrent.clients.source;

import com.adik993.mytorrent.clients.AutomaticClient;
import com.adik993.mytorrent.clients.TorrentClient;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

import static io.reactivex.Flowable.empty;
import static io.reactivex.Flowable.fromIterable;

@RequiredArgsConstructor
public class CompositeTorrentClientSource implements TorrentClientSource {
    private final List<? extends TorrentClientSource> sources;
    private final Scheduler automaticProviderScheduler;

    @Override
    public Flowable<TorrentClient> provide() {
        return fromIterable(sources)
                .flatMap(source -> source.provide().onErrorResumeNext(empty()))
                .toList()
                .flattenAsFlowable(providers -> concat(providers,
                        new AutomaticClient(providers, automaticProviderScheduler)));
    }

    private List<TorrentClient> concat(List<TorrentClient> source, TorrentClient torrentClient) {
        val list = new ArrayList<TorrentClient>(source);
        list.add(torrentClient);
        return list;
    }
}
