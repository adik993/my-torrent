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
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class TorrentsProvidersFacadeTest {
    private TorrentsProvidersFacade underTest;
    private TorrentsProvider defaultProvider = new MockProvider(0L, "aaa");
    private List<TorrentsProvider> providers = Arrays.asList(defaultProvider, new MockProvider(1L, "bbb"));
    private final List<TorrentsProvider> toBeSorted = Arrays.asList(
            MockProvider.forName("a"),
            MockProvider.forName("b"),
            MockProvider.forName("c"),
            MockProvider.forName("c"),
            MockProvider.forName("d")
    );

    @Before
    public void init() {
        underTest = new TorrentsProvidersFacade(providers, defaultProvider.getName());
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
        MockProvider bbb = spy(new MockProvider(1L, "bbb", 500));
        MockProvider ccc = spy(new MockProvider(2L, "ccc"));
        doThrow(new RuntimeException()).when(ccc).checkIfUp();
        providers = Arrays.asList(defaultProvider, bbb, ccc);
        underTest = new TorrentsProvidersFacade(providers, defaultProvider.getName());
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
        assertEquals(true, defaultProvider.isUp());
        verify(bbb, times(1)).checkIfUp();
        assertEquals(true, bbb.isUp());
        verify(ccc, times(1)).checkIfUp();
        assertEquals(false, ccc.isUp());
        verify(context, times(1)).notifyDone();
    }

    @Test
    public void updateStatusRespectMinInterval() throws Exception {
        defaultProvider = spy(new MockProvider(0L, "aaa"));
        MockProvider bbb = spy(new MockProvider(1L, "bbb"));
        providers = Arrays.asList(defaultProvider, bbb);
        underTest = new TorrentsProvidersFacade(providers, defaultProvider.getName());
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


    @Test
    public void getProvidersFirstDefaultShouldNotChangeOrder() throws Exception {
        underTest = new TorrentsProvidersFacade(toBeSorted, "a");
        performOrderTest(underTest, "a", "a", "b", "c", "d");
    }

    @Test
    public void getProvidersLastDefaultShoudBeMovedToFirstIndexRestUnchanged() throws Exception {
        underTest = new TorrentsProvidersFacade(toBeSorted, "d");
        performOrderTest(underTest, "d", "d", "a", "b", "c");
    }

    @Test
    public void getProvidersMiddleDefaultShouldBeMovedToFirstIndexRestUnchanged() throws Exception {
        underTest = new TorrentsProvidersFacade(toBeSorted, "b");
        performOrderTest(underTest, "b", "b", "a", "c", "d");
    }

    @Test
    public void getProvidersMiddleWhenDefaultNotFoundFallbackToFirst() throws Exception {
        underTest = new TorrentsProvidersFacade(toBeSorted, "e");
        performOrderTest(underTest, "a", "a", "b", "c", "d");
    }

    private void performOrderTest(TorrentsProvidersFacade facade, String expectedDefault, String... order) throws Exception {
        Iterator<TorrentsProvider> iterator = facade.getProviders().iterator();
        assertEquals("Should filter out duplicates", order.length, underTest.getProviders().size());
        for (String expected : order) {
            assertEquals(expected, iterator.next().getName());
        }
        assertEquals(expectedDefault, facade.getDefaultProvider().getName());
    }

    @Test
    public void getDefaultProvider() throws Exception {
        underTest = new TorrentsProvidersFacade(toBeSorted, "c");
        assertEquals("c", underTest.getDefaultProvider().getName());
    }

    @RequiredArgsConstructor
    private static class MockProvider extends TorrentsProvider {
        private final String name;
        private long delay = 0;

        {
            up.set(false);
        }

        MockProvider(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        MockProvider(Long id, String name, long delay) {
            this(id, name);
            this.delay = delay;
        }

        static MockProvider forName(String name) {
            return new MockProvider(0L, name, 0L);
        }

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
            if (delay > 0) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            up.set(true);
            return up.get();
        }
    }
}