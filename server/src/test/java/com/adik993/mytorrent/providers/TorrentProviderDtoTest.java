package com.adik993.mytorrent.providers;

import com.adik993.tpbclient.exceptions.ParseException;
import com.adik993.tpbclient.model.Torrent;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class TorrentProviderDtoTest {
    private TorrentProvider torrentProvider;
    private TorrentProvider torrentProvider2;

    @Before
    public void init() {
        torrentProvider = prep(3L, "aaa", true);
        torrentProvider2 = prep(1L, "bbb", false);
    }

    @Test
    public void from() throws Exception {
        TorrentProviderDto dto = TorrentProviderDto.from(torrentProvider);
        assertEquals(Long.valueOf(3L), dto.getId());
        assertEquals("aaa", dto.getName());
        assertEquals(true, dto.isUp());
    }

    @Test
    public void fromCollection() throws Exception {
        List<TorrentProviderDto> dtos = TorrentProviderDto.fromCollection(Arrays.asList(torrentProvider, torrentProvider2));
        assertEquals(Long.valueOf(3L), dtos.get(0).getId());
        assertEquals("aaa", dtos.get(0).getName());
        assertEquals(true, dtos.get(0).isUp());
        assertEquals(Long.valueOf(1L), dtos.get(1).getId());
        assertEquals("bbb", dtos.get(1).getName());
        assertEquals(false, dtos.get(1).isUp());
    }


    private TorrentProvider prep(Long id, String name, boolean isUp) {
        TorrentProvider provider = new TorrentProvider() {
            {
                up.set(isUp);
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public List<String> getCategories() {
                return null;
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
        provider.setId(id);
        return provider;
    }
}