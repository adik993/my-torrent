package com.adik993.mytorrent.filters

import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.mock.web.server.MockServerWebExchange
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilterChain
import spock.lang.Specification

class IndexWebFilterTest extends Specification {
    def underSpec = new IndexWebFilter()

    def "spring bug workaround - mutate request path to /index.html when / requested"() {
        given:
        def request = MockServerHttpRequest.get("/").build()
        def exchange = new MockServerWebExchange.Builder(request).build()
        def chain = Mock(WebFilterChain)

        when:
        underSpec.filter(exchange, chain)

        then:
        1 * chain.filter(_ as ServerWebExchange) >> { ServerWebExchange e ->
            assert e.request.path.value() == "/index.html"
        }
    }

    def "leave path unchanged if it is different than /"() {
        given:
        def request = MockServerHttpRequest.get("/api").build()
        def exchange = new MockServerWebExchange.Builder(request).build()
        def chain = Mock(WebFilterChain)

        when:
        underSpec.filter(exchange, chain)

        then:
        1 * chain.filter(_ as ServerWebExchange) >> { ServerWebExchange e ->
            assert e.request.path.value() == "/api"
        }
    }
}
