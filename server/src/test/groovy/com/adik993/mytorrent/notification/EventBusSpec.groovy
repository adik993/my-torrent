package com.adik993.mytorrent.notification

import io.reactivex.processors.PublishProcessor
import spock.lang.Specification

import java.time.LocalDateTime

import static java.time.LocalDateTime.now

class EventBusSpec extends Specification {
    def underSpec = new EventBus(PublishProcessor.create())

    def "publish event to all listeners on the topic"() {
        given:
        def sub1 = underSpec.on("a", String.class).test()
        def sub2 = underSpec.on("a", String.class).test()
        def sub3 = underSpec.on("b", String.class).test()

        when:
        underSpec.notify("a", "event")

        then:
        sub1.assertValueCount(1)
        sub1.assertValueAt(0, "event")
        sub2.assertValueCount(1)
        sub2.assertValueAt(0, "event")
        sub3.assertValueCount(0)
    }

    def "publish event to listeners on topic additionaly filtered by class type"() {
        given:
        def sub1 = underSpec.on("a", String.class).test()
        def sub2 = underSpec.on("a", LocalDateTime.class).test()
        def now = now()

        when:
        underSpec.notify("a", "event")
        underSpec.notify("a", now)

        then:
        sub1.assertValueCount(1)
        sub1.assertValueAt(0, "event")
        sub2.assertValueCount(1)
        sub2.assertValueAt(0, now)
    }

    def "publish void event"() {
        given:
        def sub1 = underSpec.on("a", Object.class).test()

        when:
        underSpec.notify("a")

        then:
        sub1.assertValueCount(1)
    }

    def "throw IllegalArgumentException when null passed to notify"() {
        when:
        underSpec.notify("a", null)

        then:
        thrown IllegalArgumentException
    }
}
