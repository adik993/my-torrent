package com.adik993.mytorrent.notification.contexts

import com.adik993.mytorrent.clients.TorrentClient
import com.adik993.mytorrent.notification.EventBus
import spock.lang.Specification

import static com.adik993.mytorrent.notification.Topic.UPDATE_CLIENTS

class SearchContextSpec extends Specification {
    def eventBus = Mock(EventBus)
    def underSpec = new SearchContext(eventBus)

    def "notify search failed notifies event bus"() {
        given:
        def client = Stub(TorrentClient)

        when:
        underSpec.notifySearchFailed(client)

        then:
        1 * eventBus.notify(UPDATE_CLIENTS, client)
    }
}
