package com.adik993.mytorrent.clients;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class TorrentClientFacadeConfig {
    @Value("#{${tc.clients.update-interval}}")
    private long updateInterval;
    @Value("#{${tc.clients.min-update-interval}}")
    private long minUpdateInterval;
    @Value("${tc.clients.default}")
    private String defaultProviderName;
    @Value("${tc.clients.update-clients.paralellism}")
    private int updateClientsParallelism;
    @Value("${tc.clients.update-on-startup}")
    private boolean updateOnStartup;
}
