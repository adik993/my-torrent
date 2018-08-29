package com.adik993.mytorrent


import spock.lang.Ignore
import spock.lang.Specification

class MyTorrentApplicationSpec extends Specification {

    @Ignore
    def "main"() {
        expect:
        MyTorrentApplication.main(["--spring.main.web-environment=false"].toArray(new String[0]))
    }
}
