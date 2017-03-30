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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private Map<Integer, Proxy> proxyMap;

    @PostConstruct
    public void init() {
        log.debug("Initializing proxies");
        proxies.add(tpborg);
        Optional<List<Proxy>> optional = Optional.ofNullable(proxyList.getProxyListIfPresent());
        optional.ifPresent(proxies::addAll);
        final int[] id = {0};
        proxyMap = proxies.stream()
                .peek(proxy -> proxy.setId(id[0]++))
                .collect(Collectors.toMap(Proxy::getId, proxy -> proxy));
        log.debug("Proxies initialized");
    }

    public List<Proxy> getProxyList() {
        return proxies;
    }

    public Proxy getProxy(Integer id) {
        return proxyMap.getOrDefault(id, tpborg);
    }
}
