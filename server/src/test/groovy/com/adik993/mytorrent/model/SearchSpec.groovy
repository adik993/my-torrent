package com.adik993.mytorrent.model

import com.adik993.mytorrent.clients.Page
import com.adik993.tpbclient.model.Torrent
import spock.lang.Specification

import static java.time.LocalDateTime.now

class SearchSpec extends Specification {
    def underSpec = new Search()

    def "create from query"() {
        when:
        def result = new Search("aaa")

        then:
        result.query == "aaa"
        result.timestamp != null
        !result.success
    }

    def "mark as successful"() {
        when:
        underSpec.success()

        then:
        underSpec.success
    }

    def "add results"() {
        given:
        def page = new Page<>(1, 2, [Stub(Torrent) {
            getTitle() >> "aaa"
            getPublishDate() >> now()
        }])

        when:
        def result = underSpec.addResults(page).block()

        then:
        underSpec.searchResults.size() == 1
        underSpec.searchResults.title == ["aaa"]
        result.is(underSpec)
    }

    def "update selection"() {
        given:
        def results = [new SearchResult(chosen: false), new SearchResult(chosen: true), new SearchResult(chosen: false)]
        underSpec.searchResults = results

        when:
        underSpec.updateSelection(results[idx].id, selected).block()

        then:
        underSpec.searchResults.id == results.id
        underSpec.searchResults.chosen == expected

        where:
        idx | selected | expected
        0   | true     | [true, false, false]
        1   | false    | [false, false, false]
    }

    def "get results count"() {
        given:
        underSpec.searchResults = [new SearchResult(), new SearchResult()]

        when:
        def result = underSpec.resultsCount

        then:
        result == 2
    }
}
