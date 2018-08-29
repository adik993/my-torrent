package com.adik993.mytorrent.clients.source

import com.adik993.tpbclient.proxy.ProxyList
import com.adik993.tpbclient.proxy.model.Proxy
import io.reactivex.Flowable
import spock.lang.Specification

class TpbClientSourceSpec extends Specification {
    def proxyList = Mock(ProxyList)
    def underSpec = new TpbClientSource(proxyList)

    def "return client for each proxy plus thepiratebay.org"() {
        given:
        def proxies = [new Proxy("a.com", 0.0f, true, "PL", true),
                       new Proxy("b.com", 0.0f, true, "PL", true)]

        when:
        def subscriber = underSpec.provide().test()
        subscriber.awaitTerminalEvent()

        then:
        1 * proxyList.fetchProxies() >> Flowable.fromIterable(proxies)
        subscriber.assertNoErrors()
        def result = subscriber.values()
        result.size() == 3
        result.name == ["thepiratebay.org", "a.com", "b.com"]
    }

    def "return only thepiratebay.org when error occured during proxy fetching"() {
        when:
        def subscriber = underSpec.provide().test()

        then:
        1 * proxyList.fetchProxies() >> Flowable.error(new IOException("failed"))
        subscriber.assertNoErrors()
        def result = subscriber.values()
        result.size() == 1
        result.name == ["thepiratebay.org"]
    }
}
