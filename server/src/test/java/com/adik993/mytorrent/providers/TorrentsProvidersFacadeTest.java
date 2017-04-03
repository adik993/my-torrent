package com.adik993.mytorrent.providers;

import com.adik993.mytorrent.notification.contexts.ProviderUpdateContext;
import com.adik993.tpbclient.exceptions.ParseException;
import com.adik993.tpbclient.model.Torrent;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class TorrentsProvidersFacadeTest {
    private TorrentsProvidersFacade underTest;
    private TorrentsProvider defaultProvider = new MockProvider(0L, "aaa");
    private List<TorrentsProvider> providers = Arrays.asList(defaultProvider, new MockProvider(1L, "bbb"));

    @Before
    public void init() {
        underTest = new TorrentsProvidersFacade(providers, defaultProvider);
    }

    @Test
    public void get() throws Exception {
        assertEquals(defaultProvider, underTest.get(null));
        assertEquals(providers.get(1), underTest.get(1L));

    }

    @Test
    public void getProviders() throws Exception {
        assertArrayEquals(providers.toArray(), underTest.getProviders().toArray());
    }

    @Test
    public void updateUpStatus() throws Exception {
        defaultProvider = spy(new MockProvider(0L, "aaa"));
        MockProvider bbb = spy(new MockProvider(1L, "bbb"));
        providers = Arrays.asList(defaultProvider, bbb);
        underTest = new TorrentsProvidersFacade(providers, defaultProvider);
        underTest.setMinUpdateInterval(0);
        ProviderUpdateContext context = mock(ProviderUpdateContext.class);
        doAnswer(invocation -> {
            Thread.sleep(1000L);
            return null;
        }).when(context).notifyDone();
        Observable<Integer> obs1 = Observable.fromCallable(() -> {
            underTest.updateUpStatus(context);
            return 1;
        }).subscribeOn(Schedulers.newThread());
        Observable<Integer> obs2 = Observable.fromCallable(() -> {
            underTest.updateUpStatus(context);
            return 1;
        }).subscribeOn(Schedulers.newThread());
        Observable.merge(Arrays.asList(obs1, obs2)).blockingSubscribe();
        verify(defaultProvider, times(1)).checkIfUp();
        verify(bbb, times(1)).checkIfUp();
        verify(context, times(1)).notifyDone();
    }

    @Test
    public void updateStatusRespectMinInterval() throws Exception {
        defaultProvider = spy(new MockProvider(0L, "aaa"));
        MockProvider bbb = spy(new MockProvider(1L, "bbb"));
        providers = Arrays.asList(defaultProvider, bbb);
        underTest = new TorrentsProvidersFacade(providers, defaultProvider);
        underTest.setMinUpdateInterval(1000);
        ProviderUpdateContext context = mock(ProviderUpdateContext.class);
        doAnswer(invocation -> {
            Thread.sleep(600L);
            return null;
        }).when(context).notifyDone();
        underTest.updateUpStatus(context);
        underTest.updateUpStatus(context);
        Thread.sleep(410L);
        underTest.updateUpStatus(context);
        verify(defaultProvider, times(2)).checkIfUp();
        verify(bbb, times(2)).checkIfUp();
        verify(context, times(2)).notifyDone();

    }

    @RequiredArgsConstructor
    private class MockProvider extends TorrentsProvider {
        private final Long id;
        private final String name;

        @Override
        public Long getId() {
            return id;
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
    }
}