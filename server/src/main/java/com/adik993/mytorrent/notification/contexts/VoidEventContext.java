package com.adik993.mytorrent.notification.contexts;


import reactor.bus.EventBus;

@SuppressWarnings("WeakerAccess")
public class VoidEventContext extends EventContext<Void> {

    public VoidEventContext(EventBus eventBus) {
        super(eventBus);
    }
}
