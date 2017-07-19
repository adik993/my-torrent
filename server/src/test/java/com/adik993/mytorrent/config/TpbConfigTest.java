package com.adik993.mytorrent.config;

import com.adik993.tpbclient.proxy.ProxyList;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TpbConfigTest {

    private TpbConfig underTest;
    private HttpClient httpClient;

    @Before
    public void setUp() {
        underTest = new TpbConfig();
        httpClient = mock(HttpClient.class);
    }

    @Test
    public void proxyList() throws Exception {
        when(httpClient.execute(any(HttpGet.class))).thenThrow(new IOException());
        ProxyList proxyList = underTest.proxyList(httpClient);
        assertNull(proxyList.getProxyListIfPresent());
    }

}