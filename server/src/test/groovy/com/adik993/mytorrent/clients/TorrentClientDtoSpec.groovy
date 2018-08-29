package com.adik993.mytorrent.clients

import spock.lang.Specification

class TorrentClientDtoSpec extends Specification {
    def "convert client to dto"() {
        given:
        def client = Stub(TorrentClient) {
            getId() >> "randomId"
            getName() >> "a.com"
            isUp() >> true
        }

        when:
        def result = new TorrentClientDto(client)

        then:
        result.id == "randomId"
        result.name == "a.com"
        result.up
    }
}
