package com.adik993.mytorrent.notification.contexts;


import reactor.bus.EventBus;

public class VoidEventContext extends EventContext<Void> {

    public VoidEventContext(EventBus eventBus, String topic) {
        super(eventBus, topic);
    }
}
