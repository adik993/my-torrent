package com.adik993.mytorrent.repository;

import com.adik993.mytorrent.model.Search;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface SearchRepository extends ReactiveMongoRepository<Search, String> {
    @Query("{'searchResults.id': ?0}")
    Mono<Search> findBySearchResultId(String id);
}
