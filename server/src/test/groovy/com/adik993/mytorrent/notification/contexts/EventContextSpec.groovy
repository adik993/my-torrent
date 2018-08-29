package com.adik993.mytorrent.notification.contexts

import com.adik993.mytorrent.notification.EventBus
import spock.lang.Specification

class EventContextSpec extends Specification {
    def eventBus = Mock(EventBus)
    def underSpec = new EventContext<Object>(eventBus)

    def "notify bus with object"() {
        given:
        def obj = new Object()

        when:
        underSpec.notifyBus("aaa", obj)

        then:
        1 * eventBus.notify("aaa", obj)
    }

    def "notify buss no object"() {
        when:
        underSpec.notifyBus("aaa")

        then:
        1 * eventBus.notify("aaa")
    }
}
