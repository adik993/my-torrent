package com.adik993.mytorrent.services;

import com.adik993.mytorrent.model.Search;
import com.adik993.mytorrent.model.SearchResult;
import com.adik993.mytorrent.repository.SearchRepository;
import com.adik993.tpbclient.TpbClient;
import com.adik993.tpbclient.exceptions.ParseException;
import com.adik993.tpbclient.model.TpbResult;
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
    private final ProxyService proxyService;
    private final SearchRepository searchRepository;

    public List<SearchResult> search(String query, Integer proxyId) throws IOException, ParseException {
        Search search = saveQuery(query, proxyId);
        TpbClient client = TpbClient.withHost(proxyService.getProxy(proxyId).getDomain());
        log.debug("Calling search on proxyId {}", proxyId);
        TpbResult result = client.search(query, null, null, null);
        log.debug("Call successful with result {}", result);
        return saveSearchResults(result, search);
    }

    private Search saveQuery(String query, Integer proxy) {
        log.debug("Searching {} on proxy {}", query, proxy);
        Search search = new Search();
        search.setQuery(query);
        search.setTimestamp(LocalDateTime.now());
        searchRepository.save(search);
        log.debug("Saved search request {}", search);
        return search;
    }

    private List<SearchResult> saveSearchResults(TpbResult result, Search search) {
        log.debug("Saving search results");
        List<SearchResult> searchResults = SearchResult.fromList(result.getTorrents(), search);
        search.setSearchResults(searchResults);
        search.setSuccess(true);
        searchRepository.save(search);
        log.debug("Saved search results");
        return searchResults;
    }
}
