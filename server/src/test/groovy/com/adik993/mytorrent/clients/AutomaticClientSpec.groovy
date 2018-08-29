package com.adik993.mytorrent.clients

import com.adik993.tpbclient.model.Torrent
import groovy.time.TimeCategory
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import spock.lang.Specification

import static java.util.concurrent.TimeUnit.MILLISECONDS

class AutomaticClientSpec extends Specification {
    def client1 = Mock(TorrentClient)
    def client2 = Mock(TorrentClient)
    def underSpec = new AutomaticClient([client1, client2], Schedulers.io())


    def "run clients concurrently and return first result"() {
        given:
        def query = "fancy query"
        def category = "category"
        def page = 1
        def orderBy = "orderBy"
        def client1Response = new Page(1, 1, [])
        def client2Response = new Page(1, 2, [])

        when:
        TestObserver<Page<Torrent>> subscriber = null
        def duration = elapsedTime {
            subscriber = underSpec.search(query, category, page, orderBy).test()
            subscriber.awaitTerminalEvent()
        }

        then:
        1 * client1.search(query, category, page, orderBy) >> Single.just(client1Response).delay(1000, MILLISECONDS)
        1 * client2.search(query, category, page, orderBy) >> Single.just(client2Response).delay(500, MILLISECONDS)
        subscriber.assertNoErrors()
        def result = subscriber.values()
        result.size() == 1
        result[0].is(client2Response)
        duration.millis <= 600
    }

    def "continue if some clients failed"() {
        given:
        def query = "fancy query"
        def category = "category"
        def page = 1
        def orderBy = "orderBy"
        def response = new Page(1, 1, [])

        when:
        def subscriber = underSpec.search(query, category, page, orderBy).test()
        subscriber.awaitTerminalEvent()

        then:
        1 * client1.search(query, category, page, orderBy) >> Single.just(response).delay(500, MILLISECONDS)
        1 * client2.search(query, category, page, orderBy) >> Single.error(new IOException("failed to fetch"))
        subscriber.assertNoErrors()
        def result = subscriber.values()
        result.size() == 1
        result[0].is(response)
    }

    def "fail if all subscribers failed"() {
        given:
        def query = "fancy query"
        def category = "category"
        def page = 1
        def orderBy = "orderBy"

        when:
        def subscriber = underSpec.search(query, category, page, orderBy).test()
        subscriber.awaitTerminalEvent()

        then:
        1 * client1.search(query, category, page, orderBy) >> Single.error(new IOException("failed for w/e reason"))
        1 * client2.search(query, category, page, orderBy) >> Single.error(new IOException("failed to fetch"))
        subscriber.assertError(NoSuchElementException)
    }

    def "return empty categories list"() {
        when:
        def result = underSpec.categories

        then:
        result.empty
    }

    def "check if up always returns true"() {
        when:
        def result = underSpec.checkIfUp().blockingGet()

        then:
        result.up
    }

    def elapsedTime(Closure closure) {
        def timeStart = new Date()
        closure()
        def timeStop = new Date()
        TimeCategory.minus(timeStop, timeStart)
    }
}
