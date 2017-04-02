package com.adik993.mytorrent.notification.contexts;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reactor.bus.Event;
import reactor.bus.EventBus;

@RequiredArgsConstructor
@Getter(AccessLevel.PUBLIC)
public class EventContext<T> {
    private final EventBus eventBus;
    private final String topic;

    protected void notifyBus(T object) {
        eventBus.notify(topic, Event.wrap(object));
    }

    protected void notifyBus() {
        eventBus.notify(topic, Event.wrap(new Object()));
    }
}
