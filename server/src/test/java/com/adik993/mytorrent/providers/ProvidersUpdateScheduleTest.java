package com.adik993.mytorrent.providers;

import com.adik993.mytorrent.notification.EventContextFactory;
import com.adik993.mytorrent.websocket.WebSocketSessionManager;
import org.junit.Before;
import org.junit.Test;

import static com.adik993.mytorrent.websocket.WebSocketTopic.PROVIDERS;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ProvidersUpdateScheduleTest {
    private ProvidersUpdateSchedule underTest;
    private TorrentsProvidersFacade torrentsProvidersFacade;
    private EventContextFactory eventContextFactory;
    private WebSocketSessionManager webSocketSessionManager;

    @Before
    public void init() {
        torrentsProvidersFacade = mock(TorrentsProvidersFacade.class);
        eventContextFactory = mock(EventContextFactory.class);
        webSocketSessionManager = mock(WebSocketSessionManager.class);
        when(webSocketSessionManager.getConnectionCount(PROVIDERS)).thenReturn(1L);
        underTest = new ProvidersUpdateSchedule(torrentsProvidersFacade, eventContextFactory, webSocketSessionManager);
    }

    @Test
    public void updateProvidersWithSomeSubscribers() throws Exception {
        underTest.updateProviders();
        verify(torrentsProvidersFacade, times(1)).updateUpStatus(any());
    }

    @Test
    public void updateProvidersWithNoSubscribers() throws Exception {
        when(webSocketSessionManager.getConnectionCount(PROVIDERS)).thenReturn(0L);
        underTest.updateProviders();
        verify(torrentsProvidersFacade, times(0)).updateUpStatus(any());
    }

}