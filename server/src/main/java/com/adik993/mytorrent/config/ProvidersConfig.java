package com.adik993.mytorrent.config;

import com.adik993.mytorrent.providers.TorrentsProvider;
import com.adik993.mytorrent.providers.TorrentsProvidersFacade;
import com.adik993.mytorrent.providers.TpbProvider;
import com.adik993.mytorrent.services.ProxyService;
import com.adik993.tpbclient.TpbClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class ProvidersConfig {

    @Bean
    public TorrentsProvidersFacade torrentsProvidersFacade(ProxyService proxyService) {
        List<TorrentsProvider> providers = new ArrayList<>();
        TorrentsProvider defaultProvider;
        final long[] id = {0};
        List<TpbProvider> tpbProviders = proxyService.getProxyList().stream()
                .map(proxy -> new TpbProvider(TpbClient.withHost(proxy.getDomain())))
                .peek(tp -> tp.setId(id[0]++))
                .collect(Collectors.toList());
        defaultProvider = tpbProviders.isEmpty() ? null : tpbProviders.get(0);
        providers.addAll(tpbProviders);
        return new TorrentsProvidersFacade(providers, defaultProvider);
    }
}
