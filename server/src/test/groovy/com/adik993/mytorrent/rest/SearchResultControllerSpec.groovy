package com.adik993.mytorrent.rest

import com.adik993.mytorrent.model.Search
import com.adik993.mytorrent.services.SelectService
import com.pchudzik.springmock.infrastructure.annotation.AutowiredMock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriBuilder
import reactor.core.publisher.Mono
import spock.lang.Specification

@WebFluxTest(SearchResultController)
class SearchResultControllerSpec extends Specification {
    @Autowired
    WebTestClient webTestClient

    @AutowiredMock
    SelectService service

    def "select - success"() {
        given:
        def id = "id"
        def selected = true

        when:
        WebTestClient.ResponseSpec result = webTestClient.post()
                .uri { UriBuilder builder ->
            builder.path("/api/result/select")
                    .queryParam("id", id)
                    .queryParam("selected", selected).build()
        }.exchange()

        then:
        1 * service.select(id, selected) >> Mono.just(new Search("query"))
        result.expectStatus().isNoContent()
        result.expectBody().isEmpty()
    }

    def "select - not found"() {
        given:
        def id = "id"
        def selected = true

        when:
        WebTestClient.ResponseSpec result = webTestClient.post()
                .uri { UriBuilder builder ->
            builder.path("/api/result/select")
                    .queryParam("id", id)
                    .queryParam("selected", selected).build()
        }.exchange()

        then:
        1 * service.select(id, selected) >> Mono.empty()
        result.expectStatus().isNotFound()
        result.expectBody().isEmpty()
    }
}
