package com.adik993.mytorrent.notification.contexts;


import lombok.RequiredArgsConstructor;
import reactor.bus.Event;
import reactor.bus.EventBus;

@SuppressWarnings("WeakerAccess")
@RequiredArgsConstructor
public class EventContext<T> {
    private final EventBus eventBus;

    protected void notifyBus(String topic, T object) {
        eventBus.notify(topic, Event.wrap(object));
    }

    protected void notifyBus(String topic) {
        eventBus.notify(topic, Event.wrap(new Object()));
    }
}
