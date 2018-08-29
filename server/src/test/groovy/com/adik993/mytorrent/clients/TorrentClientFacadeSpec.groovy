package com.adik993.mytorrent.clients

import com.adik993.mytorrent.clients.source.TorrentClientSource
import com.adik993.mytorrent.notification.EventBus
import com.adik993.tpbclient.model.Torrent
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.processors.PublishProcessor
import spock.lang.Specification
import spock.lang.Unroll

import static com.adik993.mytorrent.notification.Topic.UPDATE_CLIENTS
import static java.util.concurrent.TimeUnit.MILLISECONDS

class TorrentClientFacadeSpec extends Specification {
    def eventBus = new EventBus(PublishProcessor.create())
    def client1 = Spy(MockClient, constructorArgs: ["a"])
    def client2 = Spy(MockClient, constructorArgs: ["b"])
    def source = Mock(TorrentClientSource) {
        provide() >> Flowable.fromIterable([client1, client2])
    }
    def config = new TorrentClientFacadeConfig(defaultProviderName: "a", updateClientsParallelism: 2)
    def underSpec = new TorrentClientFacade(eventBus, source, config)

    def "refresh client list updates state with sorted list of clients"() {
        given:
        config.defaultProviderName = defaultName

        when:
        def subscriber = underSpec.refreshClientsList().test()
        subscriber.awaitTerminalEvent()

        then:
        subscriber.assertNoErrors()
        def result = underSpec.getProviders().blockingGet()
        result.name == expectedNames
        result.id.unique().size() == 2
        subscriber.values()[0].name == expectedNames

        where:
        defaultName | expectedNames
        "b"         | ["b", "a"]
        "a"         | ["a", "b"]
    }

    def "refresh client list keep old values on fail"() {
        given:
        underSpec.refreshClientsList().blockingGet()

        and:
        assert underSpec.providers.blockingGet().name == ["a", "b"]

        when:
        def subscriber = underSpec.refreshClientsList().test()
        subscriber.awaitTerminalEvent()

        then:
        1 * source.provide() >> Flowable.error(new RuntimeException("failed creating list of clients"))
        subscriber.assertError(RuntimeException)
        underSpec.providers.blockingGet().name == ["a", "b"]
    }

    def "client up status updates every specified interval"() {
        given:
        config.minUpdateInterval = 100
        config.updateInterval = 200
        config.updateClientsParallelism = 2
        underSpec = new TorrentClientFacade(eventBus, source, config)
        underSpec.refreshClientsList().blockingGet()

        when:
        def subscriber = underSpec.clientUpdates().test()
        Thread.sleep(250)
        subscriber.dispose()

        then:
        2 * client1.checkIfUp() >> Single.just(client1)
        2 * client2.checkIfUp() >> Single.just(client2)
        subscriber.assertNoErrors()
        subscriber.assertValueCount(2)

    }

    def "client updates respect minimum update interval"() {
        given:
        config.minUpdateInterval = 100
        config.updateInterval = 500
        config.updateClientsParallelism = 2
        underSpec = new TorrentClientFacade(eventBus, source, config)
        underSpec.refreshClientsList().blockingGet()

        when:
        def subscriber = underSpec.clientUpdates().test()
        Thread.sleep(50)
        eventBus.notify(UPDATE_CLIENTS, client1)
        Thread.sleep(100)
        subscriber.dispose()

        then:
        1 * client1.checkIfUp() >> Single.just(client1)
        1 * client2.checkIfUp() >> Single.just(client2)
        subscriber.assertNoErrors()
        subscriber.assertValueCount(1)
    }

    def "client updates refreshed on UPDATE_CLIENTS event"() {
        given:
        config.minUpdateInterval = 100
        config.updateInterval = 500
        config.updateClientsParallelism = 2
        underSpec = new TorrentClientFacade(eventBus, source, config)
        underSpec.refreshClientsList().blockingGet()

        when:
        def subscriber = underSpec.clientUpdates().test()
        Thread.sleep(150)
        eventBus.notify(UPDATE_CLIENTS, client1)
        Thread.sleep(50)
        subscriber.dispose()

        then:
        2 * client1.checkIfUp() >> Single.just(client1)
        2 * client2.checkIfUp() >> Single.just(client2)
        subscriber.assertNoErrors()
        subscriber.assertValueCount(2)
    }

    @Unroll
    def "client updates respect parallelism setting: #parallelism"() {
        given:
        client1.setUp(false)
        client2.setUp(false)
        config.minUpdateInterval = 100
        config.updateInterval = 10000
        config.updateClientsParallelism = parallelism
        underSpec = new TorrentClientFacade(eventBus, source, config)
        underSpec.refreshClientsList().blockingGet()

        when:
        def subscriber = underSpec.clientUpdates().test()
        Thread.sleep(150)
        subscriber.dispose()

        then:
        1 * client1.checkIfUp() >> Single.just(client1).delay(100, MILLISECONDS).doOnSuccess { c -> c.setUp(true) }
        1 * client2.checkIfUp() >> Single.just(client2).delay(100, MILLISECONDS).doOnSuccess { c -> c.setUp(true) }
        subscriber.assertNoErrors()
        client1.up
        client2.up == client2Up

        where:
        parallelism | client2Up
        2           | true
        1           | false
    }

    def "init - refreshes client list and updates theirs up status when update on startup is true"() {
        when:
        config.updateOnStartup = true
        underSpec.init()

        then:
        1 * client1.checkIfUp()
        1 * client2.checkIfUp()
        underSpec.providers.blockingGet().name == ["a", "b"]
    }

    def "init - refreshes only client list if update on startup is false"() {
        when:
        config.updateOnStartup = false
        underSpec.init()

        then:
        0 * client1.checkIfUp()
        0 * client2.checkIfUp()
        underSpec.providers.blockingGet().name == ["a", "b"]
    }

    def "get providers"() {
        given:
        underSpec.init()

        when:
        def result = underSpec.getProviders().blockingGet()

        then:
        result.size() == 2
        result.name == ["a", "b"]
    }

    def "get existing provider by id"() {
        given:
        underSpec.init()
        def existingClient = underSpec.providers.blockingGet()[0]

        when:
        def result = underSpec.get(existingClient.id)

        then:
        result.is(existingClient)
    }

    def "return default client when null passed"() {
        given:
        underSpec.init()

        when:
        def result = underSpec.get(null)

        then:
        result.name == config.defaultProviderName
    }

    def "return default client when non existing id passed"() {
        given:
        underSpec.init()

        when:
        def result = underSpec.get("nonExistingId")

        then:
        result.name == config.defaultProviderName
    }

    public static class MockClient extends TorrentClient {
        private final String name

        MockClient(String name) {
            this.name = name
        }

        void setUp(boolean val) {
            this.@up.set(val)
        }

        @Override
        String getName() {
            return name
        }

        @Override
        List<String> getCategories() {
            return emptyList()
        }

        @Override
        Single<Page<Torrent>> search(String query, String category, Integer page, String orderBy) {
            return Single.just(new Page<>(1, 1, []))
        }

        @Override
        Single<TorrentClient> checkIfUp() {
            return Single.just(this)
        }
    }
}
