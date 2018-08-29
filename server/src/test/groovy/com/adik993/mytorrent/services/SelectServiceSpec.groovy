package com.adik993.mytorrent.services

import com.adik993.mytorrent.model.Search
import com.adik993.mytorrent.model.SearchResult
import com.adik993.mytorrent.repository.SearchRepository
import reactor.core.publisher.Mono
import spock.lang.Specification

class SelectServiceSpec extends Specification {
    def repository = Mock(SearchRepository)
    def underSpec = new SelectService(repository)

    def "select result - success"() {
        given:
        def search = new Search(
                query: "a",
                searchResults: [new SearchResult()]
        )
        def id = search.searchResults[0].id
        def selected = true

        when:
        def result = underSpec.select(id, selected).block()

        then:
        1 * repository.findBySearchResultId(id) >> Mono.just(search)
        1 * repository.save({ it.query == "a" && it.searchResults.chosen == [true] } as Search) >> Mono.just(search)
        result.searchResults.chosen == [true]
    }

    def "select result - handle non existing id"() {
        given:
        def id = "nonExistingId"

        when:
        def result = underSpec.select(id, true).block()

        then:
        1 * repository.findBySearchResultId(id) >> Mono.empty()
        0 * repository.save(_ as Search)
        result == null
    }
}
