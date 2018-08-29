package com.adik993.mytorrent.repository

import com.adik993.mytorrent.clients.TorrentClientFacade
import com.adik993.mytorrent.model.Search
import com.adik993.mytorrent.model.SearchResult
import com.pchudzik.springmock.infrastructure.annotation.AutowiredMock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class SearchRepositorySpec extends Specification {
    @Autowired
    SearchRepository repository

    @AutowiredMock
    TorrentClientFacade facade

    def "find by search result id"() {
        given:
        def searchResult = new SearchResult(title: "title")
        def search = new Search("query")
        search.searchResults = [searchResult]
        repository.save(search).block()

        when:
        def result = repository.findBySearchResultId(searchResult.id).block()

        then:
        result.query == search.query
    }
}
