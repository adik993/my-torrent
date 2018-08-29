package com.adik993.mytorrent.notification.contexts;


import com.adik993.mytorrent.notification.EventBus;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("WeakerAccess")
@RequiredArgsConstructor
public class EventContext<T> {
    private final EventBus eventBus;

    protected void notifyBus(String topic, T object) {
        eventBus.notify(topic, object);
    }

    protected void notifyBus(String topic) {
        eventBus.notify(topic);
    }
}
