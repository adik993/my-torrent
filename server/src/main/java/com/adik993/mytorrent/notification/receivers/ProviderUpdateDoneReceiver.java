package com.adik993.mytorrent.notification.receivers;

import com.adik993.mytorrent.notification.Topic;
import com.adik993.mytorrent.websocket.WebSocketSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.bus.EventBus;

@Service
@Slf4j
public class ProviderUpdateDoneReceiver extends VoidReceiver {
    private final WebSocketSender webSocketSender;

    @Autowired
    public ProviderUpdateDoneReceiver(EventBus eventBus, WebSocketSender webSocketSender) {
        super(eventBus);
        this.webSocketSender = webSocketSender;
    }

    @Override
    protected void consume() {
        webSocketSender.sendProviders();
    }

    @Override
    protected String getTopic() {
        return Topic.PROVIDER_STATUS_UPDATE_DONE;
    }
}
