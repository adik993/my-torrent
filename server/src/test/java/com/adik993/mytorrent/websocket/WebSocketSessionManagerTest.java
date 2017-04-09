package com.adik993.mytorrent.websocket;

import org.junit.Before;
import org.junit.Test;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WebSocketSessionManagerTest {
    private WebSocketSessionManager underTest;

    @Before
    public void init() {
        underTest = spy(new WebSocketSessionManager());
    }

    @Test
    public void onSessionSuscribeEvent() throws Exception {
        SessionSubscribeEvent event = mock(SessionSubscribeEvent.class);

        //Duplicate message should not create new entry
        mockConnectionOnEvent(event, "aaa", "sess1");
        underTest.onSessionSuscribeEvent(event);
        assertEquals(1, underTest.getConnectionCount("aaa"));
        assertNotNull(underTest.getTopicMap("aaa").get("sess1"));
        underTest.onSessionSuscribeEvent(event);
        assertEquals(1, underTest.getConnectionCount("aaa"));
        assertNotNull(underTest.getTopicMap("aaa").get("sess1"));

        mockConnectionOnEvent(event, "aaa", "sess2");
        underTest.onSessionSuscribeEvent(event);
        assertEquals(2, underTest.getConnectionCount("aaa"));
        assertNotNull(underTest.getTopicMap("aaa").get("sess1"));
        assertNotNull(underTest.getTopicMap("aaa").get("sess2"));

        //Subscribe second topic same session
        mockConnectionOnEvent(event, "bbb", "sess1");
        underTest.onSessionSuscribeEvent(event);
        assertEquals(2, underTest.getConnectionCount("aaa"));
        assertEquals(1, underTest.getConnectionCount("bbb"));
        assertNotNull(underTest.getTopicMap("bbb").get("sess1"));
    }

    @Test
    public void onSessionUnsubscribeEvent() throws Exception {
        SessionUnsubscribeEvent event = mock(SessionUnsubscribeEvent.class);
        underTest.getTopicMap("aaa").put("sess1", "");
        underTest.getTopicMap("aaa").put("sess2", "");

        // Nothing should happen if not existing session removal requested
        mockConnectionOnEvent(event, "aaa", "asas");
        underTest.onSessionUnsubscribeEvent(event);
        assertEquals(2, underTest.getConnectionCount("aaa"));

        // Nothing should happen if non existing topic removal requested
        mockConnectionOnEvent(event, "aswqe", "asas");
        underTest.onSessionUnsubscribeEvent(event);
        assertEquals(2, underTest.getConnectionCount("aaa"));
        assertEquals(0, underTest.getConnectionCount("aswqe"));

        // Must remove existing session from map
        mockConnectionOnEvent(event, "aaa", "sess1");
        underTest.onSessionUnsubscribeEvent(event);
        assertEquals(1, underTest.getConnectionCount("aaa"));
        assertNull(underTest.getTopicMap("aaa").get("sess1"));
        assertNotNull(underTest.getTopicMap("aaa").get("sess2"));

        // Nothing happens if removal of same session requested
        underTest.onSessionUnsubscribeEvent(event);
        assertEquals(1, underTest.getConnectionCount("aaa"));
        assertNotNull(underTest.getTopicMap("aaa").get("sess2"));

        // Shoudl remove last session and map be empty
        mockConnectionOnEvent(event, "aaa", "sess2");
        underTest.onSessionUnsubscribeEvent(event);
        assertEquals(0, underTest.getConnectionCount("aaa"));
    }

    @Test
    public void onSessionDisconnectEvent() throws Exception {
        SessionDisconnectEvent event = mock(SessionDisconnectEvent.class);
        underTest.getTopicMap("aaa").put("sess1", "");
        underTest.getTopicMap("aaa").put("sess2", "");
        underTest.getTopicMap("bbb").put("sess1", "");
        underTest.getTopicMap("bbb").put("sess2", "");

        // Nothing should happen if non existing session deletion requested
        when(event.getSessionId()).thenReturn("zzz");
        underTest.onSessionDisconnectEvent(event);
        assertEquals(2, underTest.getConnectionCount("aaa"));
        assertEquals(2, underTest.getConnectionCount("bbb"));

        // Should remove session from all topics
        when(event.getSessionId()).thenReturn("sess1");
        underTest.onSessionDisconnectEvent(event);
        assertEquals(1, underTest.getConnectionCount("aaa"));
        assertNotNull(underTest.getTopicMap("aaa").get("sess2"));
        assertEquals(1, underTest.getConnectionCount("bbb"));
        assertNotNull(underTest.getTopicMap("bbb").get("sess2"));
    }

    @Test
    public void extractHeaders() throws Exception {
        AbstractSubProtocolEvent event = mock(AbstractSubProtocolEvent.class);
        underTest.extractHeaders(event);
        verify(event, times(1)).getMessage();
    }

    @Test
    public void extractConnectionInfo() throws Exception {
        AbstractSubProtocolEvent event = mock(AbstractSubProtocolEvent.class);
        StompHeaderAccessor headers = mock(StompHeaderAccessor.class);
        when(headers.getHeader(SimpMessageHeaderAccessor.DESTINATION_HEADER)).thenReturn("aaa");
        when(headers.getSessionId()).thenReturn("sess1");
        doReturn(headers).when(underTest).extractHeaders(event);
        WebSocketSessionManager.ConnectionInfo connectionInfo = underTest.extractConnectionInfo(event);
        assertEquals("aaa", connectionInfo.getTopic());
        assertEquals("sess1", connectionInfo.getSessionId());
    }

    @Test
    public void getConnectionCount() throws Exception {
        underTest.getTopicMap("aaa").put("sess1", "");
        underTest.getTopicMap("aaa").put("sess2", "");
        underTest.getTopicMap("bbb").put("sess1", "");
        assertEquals(2, underTest.getConnectionCount("aaa"));
        assertEquals(1, underTest.getConnectionCount("bbb"));
        assertEquals(0, underTest.getConnectionCount("zzz"));
    }

    private void mockConnectionOnEvent(AbstractSubProtocolEvent event, String topic, String sessionId) {
        doReturn(new WebSocketSessionManager.ConnectionInfo(topic, sessionId)).when(underTest).extractConnectionInfo(event);
    }
}