package com.adik993.mytorrent.config;

import com.adik993.mytorrent.providers.AutomaticProvider;
import com.adik993.mytorrent.providers.TorrentProvider;
import com.adik993.mytorrent.providers.TorrentProviderFacade;
import com.adik993.mytorrent.providers.TpbProvider;
import com.adik993.mytorrent.services.TpbProxyService;
import com.adik993.tpbclient.TpbClient;
import com.adik993.tpbclient.proxy.model.Proxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class ProvidersConfig {

    @Bean
    public TorrentProviderFacade torrentsProvidersFacade(TpbProxyService tpbProxyService, @Value("${tc.providers.default}") String defaultProviderName) {
        List<TorrentProvider> providers = tpbProxyService.getProxyList().stream()
                .map(Proxy::getDomain)
                .map(TpbClient::withHost)
                .map(TpbProvider::new)
                .collect(Collectors.toList());
        providers.add(new AutomaticProvider(new ArrayList<>(providers)));
        return new TorrentProviderFacade(providers, defaultProviderName);
    }
}
