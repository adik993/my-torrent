package com.adik993.mytorrent.services;

import com.adik993.tpbclient.TpbClient;
import com.adik993.tpbclient.proxy.ProxyList;
import com.adik993.tpbclient.proxy.model.Proxy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by adrian on 28/03/17.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProxyService {
    private final ProxyList proxyList;
    private static final Proxy tpborg = Proxy.builder()
            .domain(TpbClient.DEFAULT_HOST)
            .speed(.0f)
            .build();
    private final List<Proxy> proxies = new ArrayList<>(30);
    private Map<Integer, Proxy> proxyMap;

    @PostConstruct
    public void init() {
        proxies.add(tpborg);
        Optional<List<Proxy>> optional = Optional.ofNullable(proxyList.getProxyListIfPresent());
        optional.ifPresent(proxies::addAll);
        final int[] id = {0};
        proxyMap = proxies.stream()
                .peek(proxy -> proxy.setId(id[0]++))
                .collect(Collectors.toMap(Proxy::getId, proxy -> proxy));
    }

    public List<Proxy> getProxyList() {
        return proxies;
    }

    public Proxy getProxy(Integer id) {
        return proxyMap.getOrDefault(id, tpborg);
    }
}
