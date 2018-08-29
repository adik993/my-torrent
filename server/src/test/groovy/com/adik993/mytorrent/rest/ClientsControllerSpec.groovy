package com.adik993.mytorrent.rest

import com.adik993.mytorrent.clients.TorrentClient
import com.adik993.mytorrent.clients.TorrentClientDto
import com.adik993.mytorrent.clients.TorrentClientFacade
import com.pchudzik.springmock.infrastructure.annotation.AutowiredMock
import io.reactivex.Flowable
import io.reactivex.Single
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification

import static java.util.concurrent.TimeUnit.SECONDS
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM
import static reactor.test.StepVerifier.create

@WebFluxTest(ClientsController)
class ClientsControllerSpec extends Specification {
    @Autowired
    WebTestClient webTestClient
    @AutowiredMock
    TorrentClientFacade facade

    def "get clients"() {
        when:
        WebTestClient.ResponseSpec result = webTestClient.get()
                .uri("/api/clients")
                .accept(APPLICATION_JSON_UTF8).exchange()

        then:
        1 * facade.getProviders() >> Single.just([torrentClient("id", "a", true)])
        result.expectStatus().isOk()

        and:
        def body = result.expectBodyList(TorrentClientDto).returnResult().responseBody
        body.size() == 1
        body[0].id == "id"
        body[0].name == "a"
        body[0].up
    }

    def "client updates sse"() {
        when:
        WebTestClient.ResponseSpec result = webTestClient.get()
                .uri("/api/clients")
                .accept(TEXT_EVENT_STREAM).exchange()

        then:
        1 * facade.clientUpdates() >> Flowable.interval(0, 1, SECONDS)
                .map { i -> [torrentClient("id", "name", true)] }
        result.expectStatus().isOk()

        and:
        def stream = result.returnResult(new ParameterizedTypeReference<List<TorrentClientDto>>() {}).responseBody
        create(stream)
                .expectNext([new TorrentClientDto(torrentClient("id", "name", true))])
                .thenCancel().verify()
    }

    def torrentClient(String id, String name, boolean up) {
        return Stub(TorrentClient) {
            getId() >> id
            getName() >> name
            isUp() >> up
        }
    }
}
