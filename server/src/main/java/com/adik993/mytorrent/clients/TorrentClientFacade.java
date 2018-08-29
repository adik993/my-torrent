package com.adik993.mytorrent.clients;

import com.adik993.mytorrent.clients.source.TorrentClientSource;
import com.adik993.mytorrent.notification.EventBus;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory.Builder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

import static com.adik993.mytorrent.notification.Topic.UPDATE_CLIENTS;
import static io.reactivex.Flowable.interval;
import static io.reactivex.schedulers.Schedulers.from;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Slf4j
@Service
public class TorrentClientFacade {
    private final Scheduler updateScheduler = from(newSingleThreadExecutor(new Builder()
            .namingPattern("clients-update-thread").build()));
    private final TorrentClientSource clientSource;
    private final Flowable<List<TorrentClient>> scheduledHealthCheck;
    private final Flowable<List<TorrentClient>> onDemandHealthCheck;
    private Map<String, TorrentClient> torrentProviders;
    private volatile long lastUpdate = 0;
    private final TorrentClientFacadeConfig config;

    public TorrentClientFacade(EventBus eventBus, TorrentClientSource clientSource, TorrentClientFacadeConfig config) {
        this.config = config;
        this.clientSource = clientSource;
        this.onDemandHealthCheck = eventBus.on(UPDATE_CLIENTS, TorrentClient.class)
                .doOnNext(client -> log.debug("try to perform on demand update"))
                .flatMapMaybe(provider -> updateUpStatus())
                .publish()
                .refCount(1);
        this.scheduledHealthCheck = interval(0, config.getUpdateInterval(), MILLISECONDS)
                .flatMapMaybe(i -> updateUpStatus())
                .publish()
                .refCount(1);
    }

    @PostConstruct
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void init() {
        refreshClientsList().blockingGet();
        if (config.isUpdateOnStartup()) updateUpStatus().blockingGet();
    }

    public TorrentClient get(String id) {
        return torrentProviders.getOrDefault(id, defaultProvider());
    }

    private TorrentClient defaultProvider() {
        return this.torrentProviders.values().iterator().next();
    }

    public Single<List<TorrentClient>> getProviders() {
        return Single.just(new ArrayList<>(torrentProviders.values()));
    }

    private Maybe<List<TorrentClient>> updateUpStatus() {
        return Maybe.create(emitter -> {
            if ((System.currentTimeMillis() - lastUpdate) > config.getMinUpdateInterval()) {
                lastUpdate = System.currentTimeMillis();
                log.debug("refreshing clients up status");
                emitter.onSuccess(true);
            } else {
                log.debug("it's too early to update clients");
            }
            emitter.onComplete();
        }).subscribeOn(updateScheduler)
                .flattenAsFlowable(b -> torrentProviders.values())
                .parallel(config.getUpdateClientsParallelism(), 1)
                .runOn(Schedulers.io())
                .flatMap(tp -> tp.checkIfUp().toFlowable(), false, 1)
                .sequential(1)
                .lastElement()
                .flatMap(client -> this.getProviders().toMaybe());
    }

    public Flowable<List<TorrentClient>> clientUpdates() {
        return Flowable.merge(this.scheduledHealthCheck, this.onDemandHealthCheck);
    }

    public Single<Collection<TorrentClient>> refreshClientsList() {
        return clientSource.provide()
                .doOnSubscribe(s -> log.debug("refreshing torrent clients list"))
                .sorted(new TorrentsProviderComparator(config.getDefaultProviderName()))
                .distinct(TorrentClient::getName)
                .toMap(TorrentClient::getId, p -> p, LinkedHashMap::new)
                .observeOn(updateScheduler)
                .doOnSuccess(providers -> {
                    this.torrentProviders = providers;
                    log.debug("torrent clients list refresh succeeded: {}", providers);
                }).doOnError(e -> log.warn("Refreshing list of clients failed", e))
                .map(Map::values);
    }

    @RequiredArgsConstructor
    private static class TorrentsProviderComparator implements Comparator<TorrentClient> {
        private final String defaultProviderName;

        @Override
        public int compare(TorrentClient o1, TorrentClient o2) {
            if (defaultProviderName.equals(o1.getName())) return -1;
            else if (o1.getName().equals(o2.getName())) return 0;
            else return 1;
        }
    }
}
