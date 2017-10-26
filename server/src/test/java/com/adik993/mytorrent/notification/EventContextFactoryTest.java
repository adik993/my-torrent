package com.adik993.mytorrent.notification;

import com.adik993.mytorrent.notification.contexts.ProviderUpdateContext;
import com.adik993.mytorrent.notification.contexts.SearchContext;
import org.junit.Before;
import org.junit.Test;
import reactor.bus.Event;
import reactor.bus.EventBus;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class EventContextFactoryTest {
    private EventBus eventBus;
    private EventContextFactory underTest;

    @Before
    public void init() {
        eventBus = mock(EventBus.class);
        underTest = new EventContextFactory(eventBus);
    }

    @Test
    public void createProviderUpdateContext() throws Exception {
        ProviderUpdateContext context = underTest.createProviderUpdateContext();
        context.notifyDone();
        verify(eventBus, timeout(1)).notify(any(String.class), any(Event.class));
    }

    @Test
    public void createSearchContext() throws Exception {
        SearchContext context = underTest.createSearchContext();
        context.notifySearchFailed(null);
        verify(eventBus, timeout(1)).notify(any(String.class), any(Event.class));
    }

}