package com.adik993.mytorrent.rest;

import com.adik993.mytorrent.services.ProxyService;
import com.adik993.tpbclient.proxy.model.Proxy;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * Created by adrian on 28/03/17.
 */
@RestController
@RequestMapping("/api/proxy")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProxyController {
    private final ProxyService proxyService;

    @GetMapping
    public Collection<Proxy> getProxies() {
        return proxyService.getProxyList();
    }
}
