package com.adik993.mytorrent.notification.contexts;

import reactor.bus.EventBus;

public class ProviderUpdateContext extends VoidEventContext {
    public ProviderUpdateContext(EventBus eventBus, String topic) {
        super(eventBus, topic);
    }

    public void notifyDone() {
        notifyBus();
    }
}
