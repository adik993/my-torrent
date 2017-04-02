package com.adik993.mytorrent.notification;

import com.adik993.mytorrent.notification.contexts.ProviderUpdateContext;
import com.adik993.mytorrent.notification.contexts.SearchContext;
import org.junit.Before;
import org.junit.Test;
import reactor.bus.EventBus;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

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
        assertEquals(eventBus, context.getEventBus());
        assertEquals(Topic.PROVIDER_STATUS_UPDATE_DONE, context.getTopic());
    }

    @Test
    public void createSearchContext() throws Exception {
        SearchContext context = underTest.createSearchContext();
        assertEquals(eventBus, context.getEventBus());
        assertEquals(Topic.PROVIDER_SEARCH_FAILED, context.getTopic());
    }

}