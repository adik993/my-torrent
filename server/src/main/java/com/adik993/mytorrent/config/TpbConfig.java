package com.adik993.mytorrent.config;

import com.adik993.tpbclient.proxy.ProxyList;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@Slf4j
public class TpbConfig {

    @Bean
    public ProxyList proxyList(HttpClient httpClient) {
        ProxyList proxyList = new ProxyList(httpClient);
        try {
            proxyList.init();
        } catch (IOException e) {
            log.error("Could not initialize proxy list");
        }
        return proxyList;
    }

    @Bean
    HttpClient httpClient() {
        return HttpClientBuilder.create().build();
    }
}
