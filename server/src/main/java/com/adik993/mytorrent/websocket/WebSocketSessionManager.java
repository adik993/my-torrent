package com.adik993.mytorrent.websocket;

import lombok.Getter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.concurrent.ConcurrentHashMap;

@ManagedResource
@Component
@Slf4j
public class WebSocketSessionManager {
    @Getter(onMethod = @__(@ManagedAttribute))
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, String>> sessions = new ConcurrentHashMap<>();

    @EventListener
    void onSessionSuscribeEvent(SessionSubscribeEvent event) {
        ConnectionInfo connectionInfo = extractConnectionInfo(event);
        log.debug("WebSocket subscribe: {}", connectionInfo);
        getTopicMap(connectionInfo.getTopic()).put(connectionInfo.getSessionId(), "");
    }

    @EventListener
    void onSessionUnsubscribeEvent(SessionUnsubscribeEvent event) {
        ConnectionInfo connectionInfo = extractConnectionInfo(event);
        log.debug("WebSocket unsubscribe: {}", connectionInfo);
        getTopicMap(connectionInfo.getTopic()).remove(connectionInfo.getSessionId());
    }

    @EventListener
    void onSessionDisconnectEvent(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        log.debug("WebSocket disconnect({}): {}", sessionId, event);
        sessions.values().forEach(map -> map.remove(sessionId));
    }

    ConcurrentHashMap<String, String> getTopicMap(String topic) {
        return sessions.computeIfAbsent(topic, k -> new ConcurrentHashMap<>());
    }

    StompHeaderAccessor extractHeaders(AbstractSubProtocolEvent event) {
        return StompHeaderAccessor.wrap(event.getMessage());
    }

    ConnectionInfo extractConnectionInfo(AbstractSubProtocolEvent event) {
        StompHeaderAccessor headers = extractHeaders(event);
        return new ConnectionInfo((String) headers.getHeader(StompHeaderAccessor.DESTINATION_HEADER), headers.getSessionId());
    }

    public long getConnectionCount(String topic) {
        return sessions.computeIfAbsent(topic, k -> new ConcurrentHashMap<>()).size();
    }

    @Value
    static class ConnectionInfo {
        String topic;
        String sessionId;
    }
}
