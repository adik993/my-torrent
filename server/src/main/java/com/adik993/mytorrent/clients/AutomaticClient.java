package com.adik993.mytorrent.clients;

import com.adik993.tpbclient.model.Torrent;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class AutomaticClient extends TorrentClient {
    private final List<? extends TorrentClient> providers;
    private final Scheduler scheduler;

    @Override
    public String getName() {
        return "automatic";
    }

    @Override
    public List<String> getCategories() {
        return Collections.emptyList();
    }

    @Override
    public Single<Page<Torrent>> search(String query, String category, Integer page, String orderBy) {
        log.debug("Searching for '{}'", query);
        return Flowable.fromIterable(providers)
                .flatMapMaybe(o -> o.search(query, category, page, orderBy).toMaybe()
                        .onErrorResumeNext(Maybe.empty())
                        .subscribeOn(scheduler))
                .firstOrError()
                .doOnSuccess(torrentPage -> log.debug("Search for '{}' done", query))
                .doOnError(throwable -> log.debug("Search for '{}' failed", query));
    }

    @Override
    public Single<TorrentClient> checkIfUp() {
        return Single.just(this);
    }
}
