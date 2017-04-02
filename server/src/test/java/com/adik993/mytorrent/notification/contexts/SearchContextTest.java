package com.adik993.mytorrent.notification.contexts;

import com.adik993.mytorrent.matchers.EventArgumentMatcher;
import com.adik993.mytorrent.providers.TpbProvider;
import org.junit.Before;
import org.junit.Test;
import reactor.bus.EventBus;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;


public class SearchContextTest {
    private static final String TOPIC = "topic";
    private EventBus eventBus;
    private SearchContext underTest;

    @Before
    public void init() {
        eventBus = mock(EventBus.class);
        underTest = new SearchContext(eventBus, TOPIC);
    }

    @Test
    public void notifySearchFailed() throws Exception {
        TpbProvider provider = new TpbProvider(null);
        underTest.notifySearchFailed(provider);
        verify(eventBus, times(1)).notify(eq(TOPIC), argThat(new EventArgumentMatcher<>(provider)));
    }

}