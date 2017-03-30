package com.adik993.mytorrent.services;

import com.adik993.mytorrent.model.Search;
import com.adik993.mytorrent.model.SearchResult;
import com.adik993.tpbclient.TpbClient;
import com.adik993.tpbclient.exceptions.ParseException;
import com.adik993.tpbclient.model.PageInfo;
import com.adik993.tpbclient.model.Torrent;
import com.adik993.tpbclient.model.TpbResult;
import com.adik993.tpbclient.proxy.model.Proxy;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SearchServiceTest {
    private SearchService underTest;
    private TpbClient tpbClient;
    private static String query = "zzz";
    private static Proxy proxy = Proxy.builder().id(0).domain("aaa.com").build();
    private static Proxy defaultProxy = Proxy.builder().id(0).domain("default.com").build();
    private static TpbResult tpbResult = new TpbResult(new PageInfo(1, 1),
            Collections.singletonList(Torrent.builder().magnetLink("aaa").build()));
    private static Search search = Search.builder().query(query).build();
    private static List<SearchResult> searchResults = Collections.singletonList(
            SearchResult.builder().magnetLink("aaa").build());

    @Before
    public void setUp() throws IOException, ParseException {
        ProxyService proxyService = mock(ProxyService.class);
        when(proxyService.getProxy(proxy.getId())).thenReturn(proxy);
        when(proxyService.getProxy(null)).thenReturn(defaultProxy);
        tpbClient = mock(TpbClient.class);
        underTest = spy(new SearchService(proxyService, null));
        doReturn(tpbClient).when(underTest).prepareClient(any());
        when(tpbClient.search(query, null, null, null)).thenReturn(tpbResult);
        doReturn(search).when(underTest).saveQuery(query, proxy.getId());
        doReturn(search).when(underTest).saveQuery(query, null);
        doReturn(searchResults).when(underTest).saveSearchResults(tpbResult, search);
    }

    @Test
    public void search() throws Exception {
        underTest.search(query, proxy.getId());
        verify(underTest, times(1)).saveQuery(query, proxy.getId());
        verify(tpbClient, times(1)).search(query, null, null, null);
        verify(underTest, times(1)).saveSearchResults(eq(tpbResult), notNull(Search.class));
    }

    @Test
    public void searchNullProxy() throws Exception {
        underTest.search(query, null);
        verify(underTest, times(1)).saveQuery(query, null);
        verify(tpbClient, times(1)).search(query, null, null, null);
        verify(underTest, times(1)).saveSearchResults(eq(tpbResult), notNull(Search.class));
    }

    @Test(expected = RuntimeException.class)
    public void searchIOException() throws Exception {
        doThrow(new RuntimeException()).when(tpbClient).search(any(), any(), any(), any());
        try {
            underTest.search(query, proxy.getId());
        } finally {
            verify(underTest, never()).saveSearchResults(any(), any());
        }
    }

    @Test
    public void prepareClient() throws Exception {
        doCallRealMethod().when(underTest).prepareClient(any());
        TpbClient client = underTest.prepareClient(proxy.getId());
        assertEquals("aaa.com", client.getHost().getHost());
    }

}