package com.adik993.mytorrent.services;

import com.adik993.mytorrent.model.Search;
import com.adik993.mytorrent.model.SearchResult;
import com.adik993.mytorrent.repository.SearchResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class SelectService {
    private final SearchResultRepository searchResultRepository;

    public void select(Long id) throws EntityNotFoundException {
        log.debug("Selecting result");
        SearchResult one = searchResultRepository.findOne(id);
        if (one == null) throw new EntityNotFoundException(String.format("Search result %s not found", id));
        log.debug("Found search result {}", one);
        Search search = one.getSearch();
        List<SearchResult> searchResults = search.getSearchResults();
        log.debug("Total results in search {}", searchResults.size());
        for (SearchResult result : searchResults) {
            if (result.getId().equals(one.getId())) {
                result.setChosen(true);
            } else {
                result.setChosen(false);
            }
        }
        searchResultRepository.save(searchResults);
        log.debug("Search results updated");
    }
}
