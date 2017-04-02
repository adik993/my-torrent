package com.adik993.mytorrent.providers;

import com.adik993.mytorrent.notification.contexts.ProviderUpdateContext;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class TorrentsProvidersFacade {
    private final Map<Long, TorrentsProvider> torrentProviders = new LinkedHashMap<>();
    private final TorrentsProvider defaultProvider;
    private final AtomicBoolean updating = new AtomicBoolean();

    public TorrentsProvidersFacade(List<TorrentsProvider> torrentsProviders, TorrentsProvider defaultProvider) {
        this.defaultProvider = defaultProvider;
        torrentsProviders.stream()
                .sorted(Comparator.comparing(TorrentsProvider::getId))
                .forEachOrdered(tp -> this.torrentProviders.put(tp.getId(), tp));
    }

    public TorrentsProvider get(Long id) {
        return torrentProviders.getOrDefault(id, defaultProvider);
    }

    public Collection<TorrentsProvider> getProviders() {
        return torrentProviders.values();
    }

    public void updateUpStatus(ProviderUpdateContext context) {
        if (updating.compareAndSet(false, true)) {
            performUpdate(context);
        }
    }

    private void performUpdate(ProviderUpdateContext context) {
        try {
            List<Observable<Boolean>> observables = torrentProviders.values().stream()
                    .map(tp -> Observable.defer(() -> Observable.just(tp.checkIfUp())))
                    .map(o -> o.subscribeOn(Schedulers.io()))
                    .collect(Collectors.toList());
            Observable.merge(observables)
                    .blockingSubscribe(b -> {
                    }, err -> {
                    });
            context.notifyDone();
        } finally {
            updating.set(false);
        }
    }
}
