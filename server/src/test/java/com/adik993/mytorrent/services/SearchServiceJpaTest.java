package com.adik993.mytorrent.services;

import com.adik993.mytorrent.model.Search;
import com.adik993.mytorrent.model.SearchResult;
import com.adik993.mytorrent.repository.SearchRepository;
import com.adik993.mytorrent.repository.SearchResultRepository;
import com.adik993.tpbclient.model.Category;
import com.adik993.tpbclient.model.PageInfo;
import com.adik993.tpbclient.model.Torrent;
import com.adik993.tpbclient.model.TpbResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SearchServiceJpaTest {
    private SearchService searchService;
    @Autowired
    private SearchRepository searchRepository;
    @Autowired
    private SearchResultRepository searchResultRepository;

    @Before
    public void setUp() {
        searchService = new SearchService(null, searchRepository);
    }

    @Test
    public void saveQuery() throws Exception {
        searchService.saveQuery("asdw", 0);
        assertEquals(1, searchRepository.count());
        Search search = searchRepository.findAll().iterator().next();
        assertEquals("asdw", search.getQuery());
        assertEquals(false, search.getSuccess());
    }

    @Test
    public void saveSearchResults() throws Exception {
        searchService.saveQuery("asdw", 0);
        Search search = searchRepository.findAll().iterator().next();
        Torrent torrent = new Torrent();
        torrent.setCategory(Category.All);
        torrent.setId(123);
        torrent.setLeeches(12);
        torrent.setMagnetLink("magnet");
        LocalDateTime publishDate = LocalDateTime.of(2017, 1, 1, 12, 15);
        torrent.setPublishDate(publishDate);
        TpbResult result = new TpbResult(new PageInfo(1, 1), Collections.singletonList(torrent));
        searchService.saveSearchResults(result, search);
        search = searchRepository.findAll().iterator().next();
        assertEquals(true, search.getSuccess());
        assertEquals(1, searchResultRepository.count());
        SearchResult searchResult = searchResultRepository.findAll().iterator().next();
        assertEquals(123L, searchResult.getTorrentId().longValue());
        assertEquals(12, searchResult.getLeeches());
        assertEquals("magnet", searchResult.getMagnetLink());
        assertEquals(publishDate, searchResult.getPublishDate());
    }
}
