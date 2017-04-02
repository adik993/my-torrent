package com.adik993.mytorrent.notification.receivers;

import com.adik993.mytorrent.matchers.SelectorArgumentMatcher;
import org.junit.Before;
import org.junit.Test;
import reactor.bus.Event;
import reactor.bus.EventBus;

import static org.mockito.Mockito.*;


public class ReceiverTest {
    public static final String TOPIC = "topic";
    private EventBus eventBus;
    private Receiver<Object> underTest;

    @Before
    public void setUp() {
        eventBus = mock(EventBus.class);
        underTest = new Receiver<Object>(eventBus) {
            @Override
            protected void consume(Object obj) {
            }

            @Override
            protected String getTopic() {
                return TOPIC;
            }
        };
    }

    @Test
    public void init() throws Exception {
        underTest.init();
        verify(eventBus, times(1)).on(argThat(new SelectorArgumentMatcher<>(TOPIC)), eq(underTest));
    }

    @Test
    public void accept() throws Exception {
        Object obj = new Object();
        underTest = spy(underTest);
        underTest.accept(Event.wrap(obj));
        verify(underTest, times(1)).consume(obj);
    }

}