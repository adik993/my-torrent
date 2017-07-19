package com.adik993.mytorrent.config;

import com.adik993.mytorrent.providers.AutomaticProvider;
import com.adik993.mytorrent.providers.TorrentsProvider;
import com.adik993.mytorrent.providers.TorrentsProvidersFacade;
import com.adik993.mytorrent.providers.TpbProvider;
import com.adik993.mytorrent.services.ProxyService;
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
    public TorrentsProvidersFacade torrentsProvidersFacade(ProxyService proxyService, @Value("${tc.providers.default}") String defaultProviderName) {
        List<TorrentsProvider> providers = proxyService.getProxyList().stream()
                .map(Proxy::getDomain)
                .map(TpbClient::withHost)
                .map(TpbProvider::new)
                .collect(Collectors.toList());
        providers.add(new AutomaticProvider(new ArrayList<>(providers)));
        return new TorrentsProvidersFacade(providers, defaultProviderName);
    }
}
