package com.adik993.mytorrent.services;

import com.adik993.mytorrent.model.Search;
import com.adik993.mytorrent.model.SearchResult;
import com.adik993.mytorrent.notification.contexts.SearchContext;
import com.adik993.mytorrent.providers.Page;
import com.adik993.mytorrent.providers.TorrentsProvider;
import com.adik993.mytorrent.providers.TorrentsProvidersFacade;
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
    private TorrentsProvider torrentsProvider;
    private TorrentsProvidersFacade torrentsProvidersFacade;
    public static final Long providerId = 0L;
    private static String query = "zzz";
    private static Page<Torrent> result = new Page<>(1, 1,
            Collections.singletonList(Torrent.builder().magnetLink("aaa").build()));
    private static Search search = Search.builder().query(query).build();
    private static List<SearchResult> searchResults = Collections.singletonList(
            SearchResult.builder().magnetLink("aaa").build());


    private void mockTorrenstProviderFacade() throws Exception {
        torrentsProvider = spy(new TpbProvider(TpbClient.withHost("aaa.com")));
        torrentsProvider.setId(providerId);
        doReturn(result).when(torrentsProvider).search(query, null, null, null);

        torrentsProvidersFacade = mock(TorrentsProvidersFacade.class);
        when(torrentsProvidersFacade.get(null)).thenReturn(torrentsProvider);
        when(torrentsProvidersFacade.get(providerId)).thenReturn(torrentsProvider);
    }

    @Before
    public void setUp() throws Exception {
        mockTorrenstProviderFacade();
        searchContext = mock(SearchContext.class);
        underTest = spy(new SearchService(null, torrentsProvidersFacade));
        doReturn(search).when(underTest).saveQuery(query, providerId);
        doReturn(search).when(underTest).saveQuery(query, null);
        doReturn(searchResults).when(underTest).saveSearchResults(result, search);
    }

    @Test
    public void search() throws Exception {
        underTest.search(searchContext, query, providerId);
        verify(underTest, times(1)).saveQuery(query, providerId);
        verify(torrentsProvider, times(1)).search(query, null, null, null);
        verify(underTest, times(1)).saveSearchResults(result, search);
    }

    @Test
    public void searchNullProxy() throws Exception {
        underTest.search(searchContext, query, null);
        verify(underTest, times(1)).saveQuery(query, null);
        verify(torrentsProvider, times(1)).search(query, null, null, null);
        verify(underTest, times(1)).saveSearchResults(result, search);
    }

    @Test(expected = RuntimeException.class)
    public void searchException() throws Exception {
        doThrow(new RuntimeException()).when(torrentsProvider).search(any(), any(), any(), any());
        try {
            underTest.search(searchContext, query, providerId);
        } finally {
            verify(searchContext, times(1)).notifySearchFailed(torrentsProvider);
            verify(underTest, never()).saveSearchResults(any(), any());
        }
    }
}