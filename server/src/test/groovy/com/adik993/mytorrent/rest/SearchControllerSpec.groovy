package com.adik993.mytorrent.rest

import com.adik993.mytorrent.model.SearchResult
import com.adik993.mytorrent.notification.EventContextFactory
import com.adik993.mytorrent.notification.contexts.SearchContext
import com.adik993.mytorrent.services.SearchService
import com.pchudzik.springmock.infrastructure.annotation.AutowiredMock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriBuilder
import reactor.core.publisher.Flux
import spock.lang.Specification
import spock.lang.Unroll

@WebFluxTest(SearchController)
class SearchControllerSpec extends Specification {
    @Autowired
    WebTestClient webTestClient
    @AutowiredMock
    SearchService service
    @AutowiredMock
    EventContextFactory factory

    @Unroll
    def "call search and return results with client id: #clientId"() {
        given:
        def query = "a"
        def context = Stub(SearchContext)

        when:
        WebTestClient.ResponseSpec result = webTestClient.get()
                .uri { UriBuilder builder ->
            builder.path("/api/search")
                    .queryParam("query", query)
                    .queryParam("client", clientId).build()
        }.exchange()

        then:
        1 * factory.createSearchContext() >> context
        1 * service.search(context, query, clientId) >> Flux.just(new SearchResult(title: "title"))
        result.expectStatus().isOk()

        and:
        def body = result.expectBodyList(SearchResult)
                .returnResult().responseBody

        body.size() == 1
        body[0].title == "title"

        where:
        clientId  | _
        "notNull" | _
        null      | _

    }

    def "search - bad request when query is not present"() {
        when:
        WebTestClient.ResponseSpec result = webTestClient.get().uri("/api/search").exchange()

        then:
        result.expectStatus().isBadRequest()
    }

}
