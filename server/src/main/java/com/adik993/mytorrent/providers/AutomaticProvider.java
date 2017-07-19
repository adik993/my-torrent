package com.adik993.mytorrent.providers;

import com.adik993.tpbclient.exceptions.ParseException;
import com.adik993.tpbclient.model.Torrent;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
public class AutomaticProvider extends TorrentsProvider {

    private List<? extends TorrentsProvider> providers;

    public AutomaticProvider(List<? extends TorrentsProvider> providers) {
        this.providers = providers;
        this.up.set(true);
    }

    @Override
    public String getName() {
        return "automatic";
    }

    @Override
    public List<String> getCategories() {
        return Collections.emptyList();
    }

    @Override
    public Page<Torrent> search(String query, String category, Integer page, String orderBy) throws IOException, ParseException {
        log.debug("Searching for {}", query);
        List<Observable<Page<Torrent>>> observables = providers.stream()
                .map(tp -> Observable.defer(() -> Observable.just(tp.search(query, category, page, orderBy))))
                .map(o -> o.onErrorResumeNext(Observable.empty()))
                .map(o -> o.subscribeOn(Schedulers.io()))
                .collect(Collectors.toList());
        try {
            return Observable.merge(observables)
                    .blockingFirst();
        } catch (NoSuchElementException e) {
            throw new IOException("Unable to reach any provider");
        } finally {
            log.debug("Search for {} done", query);
        }
    }

    @Override
    public boolean checkIfUp() {
        return true;
    }
}
