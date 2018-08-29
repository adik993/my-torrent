package com.adik993.mytorrent.services;

import com.adik993.mytorrent.model.Search;
import com.adik993.mytorrent.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class SelectService {
    private final SearchRepository searchRepository;

    public Mono<Search> select(String id, boolean selected) {
        log.debug("{} result {}", selected ? "selecting" : "deselecting", id);
        return searchRepository.findBySearchResultId(id)
                .doOnNext(search -> log.debug("Found search {} with {} results", search, search.getSearchResults().size()))
                .flatMap(search -> search.updateSelection(id, selected))
                .flatMap(searchRepository::save)
                .doOnSuccess(search -> log.debug("Search results updated for {}", search));
    }
}
