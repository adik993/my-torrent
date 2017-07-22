package com.adik993.mytorrent.services;

import com.adik993.mytorrent.model.Search;
import com.adik993.mytorrent.model.SearchResult;
import com.adik993.mytorrent.notification.contexts.SearchContext;
import com.adik993.mytorrent.providers.Page;
import com.adik993.mytorrent.providers.TorrentProvider;
import com.adik993.mytorrent.providers.TorrentProviderFacade;
import com.adik993.mytorrent.providers.TpbProvider;
import com.adik993.tpbclient.TpbClient;
import com.adik993.tpbclient.model.Torrent;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class SearchServiceTest {
    private SearchService underTest;
    private SearchContext searchContext;
    private TorrentProvider torrentProvider;
    private TorrentProviderFacade torrentProviderFacade;
    private static final Long providerId = 0L;
    private static String query = "zzz";
    private static Page<Torrent> result = new Page<>(1, 1,
            Collections.singletonList(Torrent.builder().magnetLink("aaa").build()));
    private static Search search = Search.builder().query(query).build();
    private static List<SearchResult> searchResults = Collections.singletonList(
            SearchResult.builder().magnetLink("aaa").build());


    private void mockTorrentProviderFacade() throws Exception {
        torrentProvider = spy(new TpbProvider(TpbClient.withHost("aaa.com")));
        torrentProvider.setId(providerId);
        doReturn(result).when(torrentProvider).search(query, null, null, null);

        torrentProviderFacade = mock(TorrentProviderFacade.class);
        when(torrentProviderFacade.get(null)).thenReturn(torrentProvider);
        when(torrentProviderFacade.get(providerId)).thenReturn(torrentProvider);
    }

    @Before
    public void setUp() throws Exception {
        mockTorrentProviderFacade();
        searchContext = mock(SearchContext.class);
        underTest = spy(new SearchService(null, torrentProviderFacade));
        doReturn(search).when(underTest).saveQuery(query, providerId);
        doReturn(search).when(underTest).saveQuery(query, null);
        doReturn(searchResults).when(underTest).saveSearchResults(result, search);
    }

    @Test
    public void search() throws Exception {
        underTest.search(searchContext, query, providerId);
        verify(underTest, times(1)).saveQuery(query, providerId);
        verify(torrentProvider, times(1)).search(query, null, null, null);
        verify(underTest, times(1)).saveSearchResults(result, search);
    }

    @Test
    public void searchNullProxy() throws Exception {
        underTest.search(searchContext, query, null);
        verify(underTest, times(1)).saveQuery(query, null);
        verify(torrentProvider, times(1)).search(query, null, null, null);
        verify(underTest, times(1)).saveSearchResults(result, search);
    }

    @Test(expected = RuntimeException.class)
    public void searchException() throws Exception {
        doThrow(new RuntimeException()).when(torrentProvider).search(any(), any(), any(), any());
        try {
            underTest.search(searchContext, query, providerId);
        } finally {
            verify(searchContext, times(1)).notifySearchFailed(torrentProvider);
            verify(underTest, never()).saveSearchResults(any(), any());
        }
    }
}