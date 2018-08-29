package com.adik993.mytorrent.model


import com.adik993.tpbclient.model.Torrent
import spock.lang.Specification

import static com.adik993.tpbclient.model.Category.Applications
import static com.adik993.tpbclient.model.TorrentQuality.VIP
import static java.time.LocalDateTime.now

class SearchResultSpec extends Specification {
    def "create from Torrent"() {
        given:
        def now = now()
        def torrent = Stub(Torrent) {
            getId() >> 1
            getTitle() >> "aaa"
            getCategory() >> Applications
            getUser() >> "user"
            getMagnetLink() >> "magnet:cos"
            getTorrentLink() >> "http://cos.com"
            getPublishDate() >> now
            getQuality() >> VIP
            getSize() >> 100
            getSeeds() >> 200
            getLeeches() >> 300
        }

        when:
        def result = new SearchResult(torrent)

        then:
        result.id != null
        !result.chosen
        result.torrentId == 1
        result.title == "aaa"
        result.category == Applications
        result.user == "user"
        result.magnetLink == "magnet:cos"
        result.torrentLink == "http://cos.com"
        result.publishDate == now
        result.quality == VIP
        result.size == 100
        result.seeds == 200
        result.leeches == 300
    }
}
