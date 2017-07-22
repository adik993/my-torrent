package com.adik993.mytorrent.providers;

import com.adik993.tpbclient.exceptions.ParseException;
import com.adik993.tpbclient.model.Torrent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class AutomaticProviderTest {
    private AutomaticProvider underTest;

    @Before
    public void setUp() {
        underTest = new AutomaticProvider(Collections.emptyList());
    }

    @Test
    public void getName() throws Exception {
        assertEquals("automatic", underTest.getName());
    }

    @Test
    public void getCategories() throws Exception {
        assertEquals(Collections.emptyList(), underTest.getCategories());
    }

    @Test
    public void searchWithOneFailShouldNotThrowError() throws Exception {
        List<TorrentProvider> providers = Arrays.asList(
                spy(new MockProvider(500, true)),
                spy(new MockProvider(1000, false))
        );
        underTest = new AutomaticProvider(providers);
        Page<Torrent> result = underTest.search("aaa", "bbb", 1, "ccc");
        verify(providers.get(0), times(1)).search("aaa", "bbb", 1, "ccc");
        verify(providers.get(1), times(1)).search("aaa", "bbb", 1, "ccc");
        assertNotNull(result);
    }

    @Test(expected = IOException.class)
    public void searchWithAllProvidersErredShouldThrowIOException() throws Exception {
        List<TorrentProvider> providers = Collections.singletonList(
                spy(new MockProvider(500, true))
        );
        underTest = new AutomaticProvider(providers);
        underTest.search("aaa", "bbb", 1, "ccc");
    }

    @Test
    public void searchWhenManyProvidersSuccessFirstShouldBeChosenAndRestInterrupted() throws Exception {
        List<MockProvider> providers = Arrays.asList(
                spy(new MockProvider(500, false)),
                spy(new MockProvider(1000, false))
        );
        underTest = new AutomaticProvider(providers);
        Page<Torrent> result = underTest.search("aaa", "bbb", 1, "ccc");
        verify(providers.get(0), times(1)).search("aaa", "bbb", 1, "ccc");
        verify(providers.get(1), times(1)).search("aaa", "bbb", 1, "ccc");
        assertTrue(providers.get(1).isInterrupted());
        assertNotNull(result);
    }

    @Test
    public void checkIfUp() throws Exception {
        assertTrue(underTest.checkIfUp());
        assertTrue(underTest.isUp());
    }

    @RequiredArgsConstructor
    static class MockProvider extends TorrentProvider {

        private final long timeout;
        private final boolean error;
        @Getter
        private boolean interrupted;

        @Override
        public String getName() {
            return null;
        }

        @Override
        public List<String> getCategories() {
            return null;
        }

        @Override
        public Page<Torrent> search(String query, String category, Integer page, String orderBy) throws IOException, ParseException {
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                interrupted = true;
                throw new RuntimeException(e);
            }
            if (error) {
                throw new IOException("Errored");
            } else {
                return new Page<>(0, 0, Collections.emptyList());
            }
        }

        @Override
        public boolean checkIfUp() {
            return false;
        }
    }

}