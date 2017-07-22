package com.adik993.mytorrent.config;

import com.adik993.mytorrent.providers.AutomaticProvider;
import com.adik993.mytorrent.providers.TorrentProviderFacade;
import com.adik993.mytorrent.providers.TpbProvider;
import com.adik993.mytorrent.services.TpbProxyService;
import com.adik993.tpbclient.proxy.ProxyList;
import com.adik993.tpbclient.proxy.model.Proxy;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProvidersConfigTest {

    private ProvidersConfig underTest;
    private TpbProxyService tpbProxyService;
    private List<Proxy> proxies = Arrays.asList(
            Proxy.builder().domain("a.com").build(),
            Proxy.builder().domain("b.com").build(),
            Proxy.builder().domain("c.com").build()
    );

    @Before
    public void setUp() throws Exception {
        underTest = new ProvidersConfig();
        ProxyList proxyList = mock(ProxyList.class);
        when(proxyList.getProxyListIfPresent()).thenReturn(proxies);
        tpbProxyService = new TpbProxyService(proxyList);
        tpbProxyService.init();
    }

    @Test
    public void createTorrentsProvidersFacadeAllDefinedProvidersShoulBeAvailable() throws Exception {
        TorrentProviderFacade facade = underTest.torrentsProvidersFacade(tpbProxyService, "aaa");
        assertEquals("Should have all proxied plus default tpb and automatic", proxies.size() + 2, facade.getProviders().size());
    }

    @Test
    public void createTorrentsProvidersFacadeDefaultAutomaticShouldBeFound() throws Exception {
        TorrentProviderFacade facade = underTest.torrentsProvidersFacade(tpbProxyService, "automatic");
        assertNotNull(facade.getDefaultProvider());
        assertEquals(facade.getDefaultProvider(), facade.getProviders().iterator().next());
        assertTrue("Should be instance of automatic provider", facade.getDefaultProvider() instanceof AutomaticProvider);
        assertEquals("automatic", facade.getDefaultProvider().getName());
    }

    @Test
    public void createTorrentsProvidersFacadeDefaultThepiratebayorgShouldBeFound() throws Exception {
        TorrentProviderFacade facade = underTest.torrentsProvidersFacade(tpbProxyService, "thepiratebay.org");
        assertNotNull(facade.getDefaultProvider());
        assertEquals(facade.getDefaultProvider(), facade.getProviders().iterator().next());
        assertTrue("Should be instance of TpbProvider", facade.getDefaultProvider() instanceof TpbProvider);
        assertEquals("thepiratebay.org", facade.getDefaultProvider().getName());
    }
}