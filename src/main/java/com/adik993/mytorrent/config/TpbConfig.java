package com.adik993.mytorrent.config;

import com.adik993.tpbclient.proxy.ProxyList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Created by adrian on 28/03/17.
 */
@Configuration
@Slf4j
public class TpbConfig {

    @Bean
    public ProxyList proxyList() {
        ProxyList proxyList = new ProxyList();
        try {
            proxyList.init();
        } catch (IOException e) {
            log.error("Could not initialize proxy list");
        }
        return proxyList;
    }
}
