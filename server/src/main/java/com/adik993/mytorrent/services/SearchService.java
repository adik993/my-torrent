package com.adik993.mytorrent.services;

import com.adik993.mytorrent.model.Search;
import com.adik993.mytorrent.model.SearchResult;
import com.adik993.mytorrent.notification.contexts.SearchContext;
import com.adik993.mytorrent.providers.Page;
import com.adik993.mytorrent.providers.TorrentsProvider;
import com.adik993.mytorrent.providers.TorrentsProvidersFacade;
import com.adik993.mytorrent.repository.SearchRepository;
import com.adik993.tpbclient.exceptions.ParseException;
import com.adik993.tpbclient.model.Torrent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class SearchService {
    private final SearchRepository searchRepository;
    private final TorrentsProvidersFacade torrentsProvidersFacade;

    public List<SearchResult> search(SearchContext searchContext, String query, Long providerId) throws IOException, ParseException {
        Search search = saveQuery(query, providerId);
        TorrentsProvider provider = torrentsProvidersFacade.get(providerId);
        log.debug("Calling search on provider {}", provider);
        Page<Torrent> result;
        try {
            result = provider.search(query, null, null, null);
        } catch (Exception e) {
            searchContext.notifySearchFailed(null);
            throw e;
        }
        log.debug("Call successful with result {}", result);
        return saveSearchResults(result, search);
    }

    Search saveQuery(String query, Long proxy) {
        log.debug("Searching {} on proxy {}", query, proxy);
        Search search = new Search();
        search.setQuery(query);
        search.setTimestamp(LocalDateTime.now());
        search.setSuccess(false);
        search = searchRepository.save(search);
        log.debug("Saved search request {}", search);
        return search;
    }

    List<SearchResult> saveSearchResults(Page<Torrent> result, Search search) {
        log.debug("Saving search results");
        List<SearchResult> searchResults = SearchResult.fromList(result.getContent(), search);
        search.setSearchResults(searchResults);
        search.setSuccess(true);
        searchRepository.save(search);
        log.debug("Saved search results");
        return searchResults;
    }
}
