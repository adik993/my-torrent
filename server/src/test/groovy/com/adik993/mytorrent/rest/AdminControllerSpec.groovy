package com.adik993.mytorrent.rest

import com.adik993.mytorrent.clients.TorrentClient
import com.adik993.mytorrent.clients.TorrentClientDto
import com.adik993.mytorrent.clients.TorrentClientFacade
import com.pchudzik.springmock.infrastructure.annotation.AutowiredMock
import io.reactivex.Single
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification

@WebFluxTest(AdminController)
class AdminControllerSpec extends Specification {

    @Autowired
    WebTestClient webTestClient

    @AutowiredMock
    TorrentClientFacade facade

    def "call refresh clients and return refreshed list"() {
        when:
        WebTestClient.ResponseSpec result = webTestClient.get()
                .uri("/api/admin/refresh-clients-list").exchange()


        then:
        1 * facade.refreshClientsList() >> Single.just([torrentClient("id", "a", true)])
        result.expectStatus().isOk()

        and:
        def body = result.expectBodyList(TorrentClientDto)
                .returnResult().responseBody
        body.size() == 1
        body[0].id == "id"
        body[0].name == "a"
        body[0].up
    }

    def torrentClient(String id, String name, boolean up) {
        return Stub(TorrentClient) {
            getId() >> id
            getName() >> name
            isUp() >> up
        }
    }
}
