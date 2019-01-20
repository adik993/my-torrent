package com.adik993.mytorrent


import spock.lang.Specification

class MyTorrentApplicationSpec extends Specification {

    def "main"() {
        expect:
        MyTorrentApplication.main(["--spring.main.web-environment=false",
                                   "--server.port=0",
                                   "--tc.clients.update-on-startup=false"].toArray(new String[0]))
    }
}
