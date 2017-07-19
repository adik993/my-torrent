package com.adik993.mytorrent.services;

import com.adik993.tpbclient.TpbClient;
import com.adik993.tpbclient.proxy.ProxyList;
import com.adik993.tpbclient.proxy.model.Proxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ProxyService {
    private final ProxyList proxyList;
    static final Proxy tpborg = Proxy.builder()
            .domain(TpbClient.DEFAULT_HOST)
            .speed(.0f)
            .build();
    private final List<Proxy> proxies = new ArrayList<>(30);

    @PostConstruct
    public void init() {
        log.debug("Initializing proxies");
        proxies.add(tpborg);
        Optional.ofNullable(proxyList.getProxyListIfPresent()).ifPresent(proxies::addAll);
        log.debug("Proxies initialized");
    }

    public List<Proxy> getProxyList() {
        return proxies;
    }
}
