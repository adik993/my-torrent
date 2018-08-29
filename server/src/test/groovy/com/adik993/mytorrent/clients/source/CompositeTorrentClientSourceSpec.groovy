package com.adik993.mytorrent.clients.source

import com.adik993.mytorrent.clients.TorrentClient
import io.reactivex.Flowable
import io.reactivex.Scheduler
import spock.lang.Specification

class CompositeTorrentClientSourceSpec extends Specification {
    def sources = [
            Stub(TorrentClientSource) {
                provide() >> Flowable.just(mockClient("a.com"))
            },
            Stub(TorrentClientSource) {
                provide() >> Flowable.just(mockClient("b.com"))
            }
    ]
    def scheduler = Stub(Scheduler)
    def underSpec = new CompositeTorrentClientSource(sources, scheduler)


    def "returns clients from all sources plus automatic provider"() {
        when:
        def subscriber = underSpec.provide().test()
        subscriber.awaitTerminalEvent()

        then:
        subscriber.assertNoErrors()
        def result = subscriber.values()
        result.name == ["a.com", "b.com", "automatic"]
        result[2].scheduler.is(scheduler)
    }

    def "on error omit item and continue processing"() {
        given:
        def source1 = Stub(TorrentClientSource) {
            provide() >> Flowable.just(mockClient("a.com")).doOnNext { c -> throw new IOException("error") }
        }
        def source2 = Stub(TorrentClientSource) {
            provide() >> Flowable.just(mockClient("b.com"))
        }
        underSpec = new CompositeTorrentClientSource([source1, source2], scheduler)

        when:
        def subscriber = underSpec.provide().test()
        subscriber.awaitTerminalEvent()

        then:
        subscriber.assertNoErrors()
        def result = subscriber.values()
        result.name == ["b.com", "automatic"]
    }

    def mockClient(String name) {
        return Mock(TorrentClient) {
            getName() >> name
        }
    }
}
