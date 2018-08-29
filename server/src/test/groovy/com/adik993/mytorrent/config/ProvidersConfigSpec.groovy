package com.adik993.mytorrent.config

import com.adik993.mytorrent.clients.TorrentClientFacade
import com.adik993.mytorrent.clients.source.CompositeTorrentClientSource
import com.pchudzik.springmock.infrastructure.annotation.AutowiredMock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import spock.lang.Specification

@SpringBootTest
class ProvidersConfigSpec extends Specification {
    @Autowired
    @Qualifier("automaticClientPool")
    ThreadPoolTaskExecutor automaticClientPool

    @Value("\${tc.clients.automatic-client.paralellism}")
    int automaticClientPoolSize

    @Autowired
    CompositeTorrentClientSource clientSource

    @AutowiredMock
    TorrentClientFacade facade

    def "automatic client pool size loaded from properties"() {
        expect:
        automaticClientPool.corePoolSize == automaticClientPoolSize
        automaticClientPool.maxPoolSize == automaticClientPoolSize
    }
}
