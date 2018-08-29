package com.adik993.mytorrent.clients

import com.adik993.tpbclient.model.Category
import com.adik993.tpbclient.model.PageInfo
import com.adik993.tpbclient.model.Torrent
import com.adik993.tpbclient.model.TpbResult
import io.reactivex.Single
import spock.lang.Specification
import spock.lang.Unroll

import static com.adik993.tpbclient.model.Category.Applications
import static com.adik993.tpbclient.model.Category.Video
import static com.adik993.tpbclient.model.OrderBy.DateAsc

class TpbClientSpec extends Specification {
    def tpbClient = Mock(com.adik993.tpbclient.TpbClient) {
        getHost() >> new URI("http://cos.com")
    }
    def underSpec = new TpbClient(tpbClient)

    def "categories contains all categories from enum"() {
        when:
        def result = underSpec.categories

        then:
        result == Category.values().collect { it.name() }
    }

    def "getName - returns host name"() {
        when:
        def result = underSpec.getName()

        then:
        1 * tpbClient.getHost() >> new URI("http://abcd.com")
        result == "abcd.com"
    }


    def "search - calls client and converts result"() {
        given:
        def torrent = Stub(Torrent)
        def tpbResult = Stub(TpbResult) {
            getPageInfo() >> Stub(PageInfo) {
                getPageSize() >> 1
                getTotal() >> 2
            }
            getTorrents() >> [torrent]
        }
        def query = "fancy query"
        def category = Video.name()
        def page = 1
        def orderBy = DateAsc.name()

        when:
        def subscriber = underSpec.search(query, category, page, orderBy).test()
        subscriber.awaitTerminalEvent()

        then:
        1 * tpbClient.search(query, Video, page, DateAsc) >> Single.just(tpbResult)
        subscriber.assertNoErrors()
        def result = subscriber.values()[0]
        result.pageSize == 1
        result.total == 2
        result.content.size() == 1
        result.content[0].is(torrent)
    }

    def "search - handles null"() {
        given:
        def query = "fancy query"
        def category = null
        def page = null
        def orderBy = null

        when:
        def subscriber = underSpec.search(query, category, page, orderBy).test()
        subscriber.awaitTerminalEvent()

        then:
        1 * tpbClient.search(query, null, null, null) >> Single.just(Stub(TpbResult))
        subscriber.assertNoErrors()
        subscriber.assertValueCount(1)
    }

    @Unroll
    def "check if up: #message"() {
        given:
        underSpec.@up.set(!expectedUp)

        when:
        def subscriber = underSpec.checkIfUp().test()
        subscriber.awaitTerminalEvent()

        then:
        1 * tpbClient.search(_ as String, null, null, null) >> response
        underSpec.up == expectedUp

        where:
        message                         | response                              | expectedUp
        "true when successful response" | Single.just(Stub(TpbResult))          | true
        "false when error response"     | Single.error(new IOException("fail")) | false
    }

    def "supprorts category"() {
        when:
        def result = underSpec.supportsCategory(category)

        then:
        result == expected

        where:
        category            | expected
        Applications.name() | true
        "aaa"               | false
    }
}
