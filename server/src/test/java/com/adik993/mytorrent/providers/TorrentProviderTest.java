package com.adik993.mytorrent.providers;

import com.adik993.tpbclient.exceptions.ParseException;
import com.adik993.tpbclient.model.Torrent;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TorrentProviderTest {
    private TorrentProvider underTest;

    @Before
    public void init() {
        underTest = new TorrentProvider() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public List<String> getCategories() {
                return Arrays.asList("aaa", "bbb");
            }

            @Override
            public Page<Torrent> search(String query, String category, Integer page, String orderBy) throws IOException, ParseException {
                return null;
            }

            @Override
            public boolean checkIfUp() {
                return false;
            }
        };
    }

    @Test
    public void getCategories() throws Exception {
        assertTrue(underTest.supportsCategory("aaa"));
        assertTrue(underTest.supportsCategory("bbb"));
        assertFalse(underTest.supportsCategory("ccc"));
    }

}