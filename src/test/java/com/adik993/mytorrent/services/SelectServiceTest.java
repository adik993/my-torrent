package com.adik993.mytorrent.services;

import com.adik993.mytorrent.model.Search;
import com.adik993.mytorrent.model.SearchResult;
import com.adik993.mytorrent.repository.SearchRepository;
import com.adik993.mytorrent.repository.SearchResultRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SelectServiceTest {
    @Autowired
    private SearchResultRepository searchResultRepository;
    @Autowired
    private SearchRepository searchRepository;

    private SelectService underTest;

    @Before
    public void setUp() {
        underTest = new SelectService(searchResultRepository);
        Search search = Search.builder().query("aaa").build();
        search = searchRepository.save(search);
        SearchResult a = SearchResult.builder().search(search).magnetLink("a").build();
        SearchResult b = SearchResult.builder().search(search).magnetLink("b").build();
        SearchResult c = SearchResult.builder().search(search).magnetLink("b").build();
        search.setSearchResults(new ArrayList<>(Arrays.asList(a, b, c)));
        searchRepository.save(search);
    }

    @Test
    public void select() throws Exception {
        Iterator<SearchResult> iterator = searchResultRepository.findAll().iterator();
        SearchResult first = iterator.next();
        SearchResult second = iterator.next();
        Long firstId = first.getId();
        Long secondId = second.getId();
        underTest.select(firstId, true);
        verifySingleSelected(firstId);
        underTest.select(secondId, true);
        verifySingleSelected(secondId);
        underTest.select(secondId, false);
        verifyAllUnselected();
        underTest.select(firstId, true);
        underTest.select(secondId, false);
        verifyAllUnselected();
    }

    @Test(expected = EntityNotFoundException.class)
    public void selectNotExisting() {
        underTest.select(999L, true);
    }


    private void verifySingleSelected(Long id) {
        for (SearchResult result : searchResultRepository.findAll()) {
            if (result.getId().equals(id)) {
                assertEquals(true, result.isChosen());
            } else {
                assertEquals(false, result.isChosen());
            }
        }
    }

    private void verifyAllUnselected() {
        StreamSupport.stream(searchResultRepository.findAll().spliterator(), false)
                .map(SearchResult::isChosen)
                .forEach(Assert::assertFalse);
    }
}