package com.adik993.mytorrent.services;

import com.adik993.tpbclient.proxy.ProxyList;
import com.adik993.tpbclient.proxy.model.Proxy;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProxyServiceTest {
    private ProxyService underTest;
    private List<Proxy> proxies = Arrays.asList(
            Proxy.builder().domain("aaa.com").build(),
            Proxy.builder().domain("bbb.com").build()
    );

    @Before
    public void setUp() {
        ProxyList proxyList = mock(ProxyList.class);
        when(proxyList.getProxyListIfPresent()).thenReturn(proxies);
        underTest = new ProxyService(proxyList);

    }

    @Test
    public void init() throws Exception {
        underTest.init();
        List<Proxy> proxyList = underTest.getProxyList();
        assertEquals(proxies.size() + 1, proxyList.size());
        for (int i = 0; i < proxyList.size(); i++) {
            assertEquals(i, proxyList.get(i).getId());
        }
    }

    @Test
    public void getProxyList() throws Exception {
        underTest.init();
        assertEquals(proxies.size() + 1, underTest.getProxyList().size());
    }

    @Test
    public void getProxy() throws Exception {
        underTest.init();
        assertEquals(proxies.get(0), underTest.getProxy(1));
        assertEquals(ProxyService.tpborg, underTest.getProxy(123123));
        assertEquals(ProxyService.tpborg, underTest.getProxy(null));
    }

}