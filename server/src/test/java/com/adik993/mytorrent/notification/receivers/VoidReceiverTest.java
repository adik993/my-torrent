package com.adik993.mytorrent.notification.receivers;

import org.junit.Before;
import org.junit.Test;
import reactor.bus.Event;

import static org.mockito.Mockito.*;


public class VoidReceiverTest {
    private VoidReceiver underTest;

    @Before
    public void init() {
        underTest = new VoidReceiver(null) {
            @Override
            protected void consume() {

            }

            @Override
            protected String getTopic() {
                return null;
            }
        };
    }

    @Test
    public void accept() throws Exception {
        underTest = spy(underTest);
        underTest.accept(Event.wrap(null));
        verify(underTest, times(1)).consume();
        verify(underTest, never()).consume(any());
    }

    @Test(expected = RuntimeException.class)
    public void consumeWithParameterShouldNotBeUsed() {
        underTest.consume(null);
    }

}