package com.adik993.mytorrent.notification.receivers;


import reactor.bus.Event;
import reactor.bus.EventBus;

public abstract class VoidReceiver extends Receiver<Void> {
    public VoidReceiver(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    public void accept(Event<Void> event) {
        consume();
    }

    protected abstract void consume();

    @Override
    protected void consume(Void obj) {
        throw new RuntimeException("Use consume() instead");
    }
}
