package com.adik993.mytorrent.providers;

import com.adik993.mytorrent.notification.contexts.ProviderUpdateContext;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Slf4j
public class TorrentProviderFacade {
    private final Map<Long, TorrentProvider> torrentProviders;
    @Getter
    private final TorrentProvider defaultProvider;
    private final AtomicBoolean updating = new AtomicBoolean();
    private volatile long lastUpdate = 0;
    @Setter(onMethod = @__(@Value("#{${tc.providers.min-update-interval}}")))
    private long minUpdateInterval = 30000;

    public TorrentProviderFacade(List<TorrentProvider> torrentProviders, String defaultProviderName) {
        TreeSet<TorrentProvider> providers = prepareSortedProviders(torrentProviders, defaultProviderName);
        this.defaultProvider = providers.first();
        this.torrentProviders = providers.stream()
                .collect(Collectors.toMap(TorrentProvider::getId, t -> t, (t1, t2) -> t2, LinkedHashMap::new));
    }

    public TorrentProvider get(Long id) {
        return torrentProviders.getOrDefault(id, defaultProvider);
    }

    public Collection<TorrentProvider> getProviders() {
        return torrentProviders.values();
    }

    public void updateUpStatus(ProviderUpdateContext context) {
        if (updating.compareAndSet(false, true)) {
            try {
                if ((System.currentTimeMillis() - lastUpdate) > minUpdateInterval) {
                    lastUpdate = System.currentTimeMillis();
                    performUpdate(context);
                }
            } finally {
                updating.set(false);
            }
        }
    }

    private void performUpdate(ProviderUpdateContext context) {
        List<Observable<Boolean>> observables = torrentProviders.values().stream()
                .map(tp -> Observable.defer(() -> Observable.just(tp.checkIfUp())).onErrorReturnItem(false))
                .map(o -> o.subscribeOn(Schedulers.io()))
                .collect(Collectors.toList());
        Observable.merge(observables)
                .blockingSubscribe(b -> {
                }, e -> log.error("Error updating provider statuses"));
        context.notifyDone();
    }

    private TreeSet<TorrentProvider> prepareSortedProviders(List<TorrentProvider> providers, String defaultProviderName) {
        TreeSet<TorrentProvider> set = new TreeSet<>(new TorrentsProviderComparator(defaultProviderName));
        set.addAll(providers);
        long[] id = {0};
        set.forEach(torrentsProvider -> torrentsProvider.setId(id[0]++));
        return set;
    }

    @RequiredArgsConstructor
    private static class TorrentsProviderComparator implements Comparator<TorrentProvider> {
        private final String defaultProviderName;

        @Override
        public int compare(TorrentProvider o1, TorrentProvider o2) {
            if (defaultProviderName.equals(o1.getName())) return -1;
            else if (o1.getName().equals(o2.getName())) return 0;
            else return 1;
        }
    }
}
