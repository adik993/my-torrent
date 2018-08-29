package com.adik993.mytorrent.rest;

import com.adik993.mytorrent.model.SearchResult;
import com.adik993.mytorrent.notification.EventContextFactory;
import com.adik993.mytorrent.services.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;
    private final EventContextFactory eventContextFactory;

    @GetMapping
    public Flux<SearchResult> search(@RequestParam("query") String query,
                                     @RequestParam(value = "client", required = false) String client) {
        return searchService.search(eventContextFactory.createSearchContext(), query, client);
    }
}
