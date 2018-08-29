package com.adik993.mytorrent.services;

import com.adik993.mytorrent.clients.Page;
import com.adik993.mytorrent.clients.TorrentClient;
import com.adik993.mytorrent.clients.TorrentClientFacade;
import com.adik993.mytorrent.model.Search;
import com.adik993.mytorrent.model.SearchResult;
import com.adik993.mytorrent.notification.contexts.SearchContext;
import com.adik993.mytorrent.repository.SearchRepository;
import com.adik993.tpbclient.model.Torrent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static reactor.adapter.rxjava.RxJava2Adapter.singleToMono;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {
    private final SearchRepository searchRepository;
    private final TorrentClientFacade torrentClientFacade;

    public Flux<SearchResult> search(SearchContext searchContext, String query, String clientId) {
        val client = torrentClientFacade.get(clientId);
        log.debug("Calling search {} on client {}", query, client);
        return saveQuery(query)
                .flatMap(search -> search(client, search).flatMap(search::addResults))
                .doOnSuccess(search -> log.debug("Call successful for query {} with result {}",
                        search, search.getSearchResults()))
                .doOnError(throwable -> searchContext.notifySearchFailed(client))
                .flatMapMany(this::saveSearchResults);
    }

    private Mono<Page<Torrent>> search(TorrentClient provider, Search search) {
        return singleToMono(provider.search(search.getQuery(), null, null, null));
    }

    private Mono<Search> saveQuery(String query) {
        return searchRepository.save(new Search(query))
                .doOnSuccess(s -> log.debug("Saved search request {}", s));
    }

    private Flux<SearchResult> saveSearchResults(Search searchWithResults) {
        return Mono.just(searchWithResults)
                .doOnNext(Search::success)
                .doOnNext(search -> log.debug("Saving search results for {} with {} results",
                        search, search.getResultsCount()))
                .flatMap(searchRepository::save)
                .doOnSuccess(search -> log.debug("Saved search results for {} with {}",
                        search, search.getResultsCount()))
                .flatMapIterable(Search::getSearchResults);
    }
}
