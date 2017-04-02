package com.adik993.mytorrent.providers;

import com.adik993.tpbclient.TpbClient;
import com.adik993.tpbclient.exceptions.ParseException;
import com.adik993.tpbclient.model.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class TpbProviderTest {
    private static TpbResult tpbResult = new TpbResult(new PageInfo(1, 1),
            Collections.singletonList(Torrent.builder().magnetLink("aaa").build()));
    private TpbClient tpbClient;
    private TpbProvider underTest;

    @Before
    public void init() throws IOException, ParseException {
        tpbClient = mock(TpbClient.class);
        when(tpbClient.search(any(), any(), any(), any())).thenReturn(tpbResult);
        underTest = new TpbProvider(tpbClient);
    }

    @Test
    public void getCategories() throws Exception {
        List<String> categories = Arrays.stream(Category.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        assertArrayEquals(categories.toArray(), underTest.getCategories().toArray());
    }

    @Test
    public void search() throws Exception {
        underTest.search("aaa", null, null, null);
        verify(tpbClient, times(1)).search("aaa", null, null, null);
        underTest.search("aaa", "All", 1, "NameDesc");
        verify(tpbClient, times(1)).search("aaa", Category.All, 1, OrderBy.NameDesc);
    }

    @Test
    public void checkIfUp() throws Exception {
        doThrow(new IOException()).when(tpbClient).search(any(), any(), any(), any());
        underTest.checkIfUp();
        assertEquals(false, underTest.isUp());

        doReturn(null).when(tpbClient).search(any(), any(), any(), any());
        underTest.checkIfUp();
        assertEquals(true, underTest.isUp());
    }

}