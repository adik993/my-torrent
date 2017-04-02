package com.adik993.mytorrent.notification.contexts;

import org.junit.Before;
import org.junit.Test;
import reactor.bus.Event;
import reactor.bus.EventBus;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class ProviderUpdateContextTest {
    private static final String TOPIC = "topic";
    private EventBus eventBus;
    private ProviderUpdateContext underTest;

    @Before
    public void init() {
        eventBus = mock(EventBus.class);
        underTest = new ProviderUpdateContext(eventBus, TOPIC);
    }

    @Test
    public void notifyDone() throws Exception {
        underTest.notifyDone();
        verify(eventBus, times(1)).notify(eq(TOPIC), any(Event.class));
    }

}