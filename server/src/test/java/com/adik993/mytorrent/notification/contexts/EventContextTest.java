package com.adik993.mytorrent.notification.contexts;

import com.adik993.mytorrent.matchers.EventArgumentMatcher;
import org.junit.Before;
import org.junit.Test;
import reactor.bus.Event;
import reactor.bus.EventBus;

import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class EventContextTest {
    private static final String TOPIC = "topic";
    private EventBus eventBus;
    private EventContext underTest;

    @Before
    public void init() {
        eventBus = mock(EventBus.class);
        underTest = new EventContext(eventBus, TOPIC);
    }

    @Test
    public void notifyBusVoid() throws Exception {
        underTest.notifyBus();
        verify(eventBus, times(1)).notify(eq(TOPIC), any(Event.class));
    }

    @Test
    public void notifyBus() throws Exception {
        Object obj = new Object();
        underTest.notifyBus(obj);
        verify(eventBus, times(1)).notify(eq(TOPIC), argThat(new EventArgumentMatcher<>(obj)));
    }

}