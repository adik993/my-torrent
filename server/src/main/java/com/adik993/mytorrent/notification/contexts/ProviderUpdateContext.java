package com.adik993.mytorrent.notification.contexts;

import reactor.bus.EventBus;

import static com.adik993.mytorrent.notification.Topic.PROVIDER_STATUS_UPDATE_DONE;

public class ProviderUpdateContext extends VoidEventContext {
    public ProviderUpdateContext(EventBus eventBus) {
        super(eventBus);
    }

    public void notifyDone() {
        notifyBus(PROVIDER_STATUS_UPDATE_DONE);
    }
}
