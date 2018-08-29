package com.adik993.mytorrent.config;

import com.adik993.mytorrent.clients.source.CompositeTorrentClientSource;
import com.adik993.mytorrent.clients.source.TorrentClientSource;
import com.adik993.tpbclient.proxy.ProxyList;
import io.reactivex.schedulers.Schedulers;
import lombok.val;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;

@Configuration
public class ProvidersConfig {

    @Bean
    @Primary
    public TorrentClientSource torrentClientSource(
            List<? extends TorrentClientSource> sources,
            @Qualifier("automaticClientPool") ThreadPoolTaskExecutor pool) {
        return new CompositeTorrentClientSource(sources, Schedulers.from(pool));
    }

    @Bean
    public ThreadPoolTaskExecutor automaticClientPool(
            @Value("${tc.clients.automatic-client.paralellism}") int parallelism) {
        val pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(parallelism);
        pool.setMaxPoolSize(parallelism);
        return pool;
    }

    @Bean
    public ProxyList proxyList(@Value("${tc.proxy-server.url}") String url) {
        return new ProxyList(url);
    }
}
