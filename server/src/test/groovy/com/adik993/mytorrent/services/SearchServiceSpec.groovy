package com.adik993.mytorrent.services

import com.adik993.mytorrent.clients.Page
import com.adik993.mytorrent.clients.TorrentClient
import com.adik993.mytorrent.clients.TorrentClientFacade
import com.adik993.mytorrent.model.Search
import com.adik993.mytorrent.notification.contexts.SearchContext
import com.adik993.mytorrent.repository.SearchRepository
import com.adik993.tpbclient.model.Torrent
import io.reactivex.Single
import reactor.core.publisher.Mono
import spock.lang.Specification

import static java.time.LocalDateTime.now
import static reactor.adapter.rxjava.RxJava2Adapter.fluxToFlowable
import static reactor.test.StepVerifier.create

class SearchServiceSpec extends Specification {
    def repository = Mock(SearchRepository)
    def facade = Mock(TorrentClientFacade)
    def underSpec = new SearchService(repository, facade)

    def "search - failed"() {
        given:
        def context = Mock(SearchContext)
        def client = Mock(TorrentClient)

        when:
        create(underSpec.search(context, "a", "id")).verifyError(IOException)

        then:
        1 * facade.get("id") >> client
        1 * repository.save({ it.query == "a" } as Search) >> { Search s -> Mono.just(s) }
        1 * client.search("a", null, null, null) >> Single.error(new IOException("fail"))
        1 * context.notifySearchFailed(client)
    }

    def "search - success"() {
        given:
        def context = Mock(SearchContext)
        def client = Mock(TorrentClient)
        def response = new Page<>(1, 1, [torrent("a"), torrent("b")])

        when:
        def subscriber = fluxToFlowable(underSpec.search(context, "a", "id")).test()
        subscriber.awaitTerminalEvent()

        then:
        1 * facade.get("id") >> client
        1 * repository.save({ it.query == "a" } as Search) >> { Search s -> Mono.just(s) }
        1 * client.search("a", null, null, null) >> Single.just(response)
        1 * repository.save({
            it.query == "a" && it.searchResults.title == ["a", "b"]
        } as Search) >> { Search s -> Mono.just(s) }
        0 * context.notifySearchFailed(client)
        subscriber.assertNoErrors()
        def result = subscriber.values()
        result.size() == 2
        result.title == ["a", "b"]
    }

    def torrent(String title) {
        return Stub(Torrent) {
            getTitle() >> title
            getPublishDate() >> now()
        }
    }
}
