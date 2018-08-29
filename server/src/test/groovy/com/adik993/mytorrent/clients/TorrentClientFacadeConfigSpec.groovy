package com.adik993.mytorrent.clients

import com.pchudzik.springmock.infrastructure.annotation.AutowiredMock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class TorrentClientFacadeConfigSpec extends Specification {
    @Autowired
    TorrentClientFacadeConfig config

    @Value("#{\${tc.clients.update-interval}}")
    private long updateInterval;
    @Value("#{\${tc.clients.min-update-interval}}")
    private long minUpdateInterval;
    @Value("\${tc.clients.default}")
    private String defaultProviderName;
    @Value("\${tc.clients.update-clients.paralellism}")
    private int updateClientsParallelism;
    @Value("\${tc.clients.update-on-startup}")
    private boolean updateOnStartup;


    @AutowiredMock
    TorrentClientFacade facade

    def "torrent client facade config pulled from properties"() {
        expect:
        config.updateInterval == updateInterval
        config.minUpdateInterval == minUpdateInterval
        config.defaultProviderName == defaultProviderName
        config.updateClientsParallelism == updateClientsParallelism
        config.updateOnStartup == updateOnStartup
    }
}
