package com.adik993.mytorrent.rest;

import com.adik993.mytorrent.model.SearchResult;
import com.adik993.mytorrent.notification.EventContextFactory;
import com.adik993.mytorrent.services.SearchService;
import com.adik993.tpbclient.exceptions.ParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * Created by adrian on 28/03/17.
 */
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SearchController {
    private final SearchService searchService;
    private final EventContextFactory eventContextFactory;

    @GetMapping
    public List<SearchResult> search(
            @RequestParam("query") String query,
            @RequestParam(value = "provider", required = false) Long provider) throws IOException, ParseException {
        return searchService.search(eventContextFactory.createSearchContext(), query, provider);
    }
}
