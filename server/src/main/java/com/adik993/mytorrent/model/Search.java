package com.adik993.mytorrent.model;

import com.adik993.mytorrent.clients.Page;
import com.adik993.tpbclient.model.Torrent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;
import static reactor.core.publisher.Mono.just;

@Document
@Getter
@Setter
@ToString(exclude = {"searchResults"})
@CompoundIndex(name = "search_results_idx", def = "{'searchResults.torrentId': 1}")
@NoArgsConstructor
public class Search {
    @Id
    private String id;
    private String query;
    private LocalDateTime timestamp = now();
    private boolean success;
    private List<SearchResult> searchResults = new ArrayList<>();

    public Search(String query) {
        this.query = query;
    }

    public void success() {
        this.success = true;
    }

    public Mono<Search> addResults(Page<Torrent> page) {
        return Flux.fromIterable(page.getContent())
                .map(SearchResult::new)
                .doOnNext(searchResults::add)
                .then(just(this));
    }

    public Mono<Search> updateSelection(String id, boolean selected) {
        return Flux.fromIterable(searchResults)
                .doOnNext(result -> result.setChosen(selected && result.getId().equals(id)))
                .then(just(this));
    }

    public int getResultsCount() {
        return searchResults.size();
    }
}
