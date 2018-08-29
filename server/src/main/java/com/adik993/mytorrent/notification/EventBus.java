package com.adik993.mytorrent.notification;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EventBus {
    private static final Object NULL = new Object();
    private final FlowableProcessor<Event<?>> processor;

    @SuppressWarnings("unchecked")
    public <T> Flowable<T> on(String topic, Class<T> type) {
        return processor.filter(event -> event.matchesTopic(topic))
                .filter(event -> event.matchesClass(type))
                .map(event -> (T) event.getData());
    }

    public <T> void notify(String topic, T object) {
        if (object == null) throw new IllegalArgumentException("object cannot be null");
        this.processor.onNext(new Event<>(topic, object));
    }

    public void notify(String topic) {
        notify(topic, NULL);
    }
}
