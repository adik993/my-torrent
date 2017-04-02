package com.adik993.mytorrent.notification.receivers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Consumer;

import javax.annotation.PostConstruct;

import static reactor.bus.selector.Selectors.$;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public abstract class Receiver<T> implements Consumer<Event<T>> {
    private final EventBus eventBus;

    @PostConstruct
    public void init() {
        eventBus.on($(getTopic()), this);
    }

    @Override
    public void accept(Event<T> event) {
        consume(event.getData());
    }

    protected abstract void consume(T obj);

    protected abstract String getTopic();
}
