package com.adik993.mytorrent.notification.receivers;

import com.adik993.mytorrent.notification.Topic;
import com.adik993.mytorrent.websocket.WebSocketSender;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ProviderUpdateDoneReceiverTest {
    private ProviderUpdateDoneReceiver underTest;
    private WebSocketSender webSocketSender;

    @Before
    public void setUp() {
        webSocketSender = mock(WebSocketSender.class);
        underTest = new ProviderUpdateDoneReceiver(null, webSocketSender);
    }


    @Test
    public void consume() throws Exception {
        underTest.consume();
        verify(webSocketSender, times(1)).sendProviders();
    }

    @Test
    public void getTopic() throws Exception {
        assertEquals(Topic.PROVIDER_STATUS_UPDATE_DONE, underTest.getTopic());
    }

}