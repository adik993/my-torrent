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

public class TpbProxyServiceTest {
    private TpbProxyService underTest;
    private List<Proxy> proxies = Arrays.asList(
            Proxy.builder().domain("aaa.com").build(),
            Proxy.builder().domain("bbb.com").build()
    );

    @Before
    public void setUp() {
        ProxyList proxyList = mock(ProxyList.class);
        when(proxyList.getProxyListIfPresent()).thenReturn(proxies);
        underTest = new TpbProxyService(proxyList);

    }

    @Test
    public void getProxyList() throws Exception {
        underTest.init();
        assertEquals(proxies.size() + 1, underTest.getProxyList().size());
        assertEquals(TpbProxyService.tpborg, underTest.getProxyList().get(0));
        assertEquals(proxies.get(0), underTest.getProxyList().get(1));
        assertEquals(proxies.get(1), underTest.getProxyList().get(2));
    }

}