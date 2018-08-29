package com.adik993.mytorrent.notification

import com.adik993.mytorrent.clients.TorrentClient
import spock.lang.Specification

import static com.adik993.mytorrent.notification.Topic.UPDATE_CLIENTS

class EventContextFactorySpec extends Specification {
    def eventBus = Mock(EventBus)
    def underSpec = new EventContextFactory(eventBus)

    def "create valid search context"() {
        when:
        def result = underSpec.createSearchContext()

        and:
        result.notifySearchFailed(Stub(TorrentClient))

        then:
        1 * eventBus.notify(UPDATE_CLIENTS, _ as TorrentClient)
    }
}
