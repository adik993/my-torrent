package com.adik993.mytorrent.matchers;

import lombok.RequiredArgsConstructor;
import org.mockito.ArgumentMatcher;
import reactor.bus.Event;

@SuppressWarnings("unchecked")
@RequiredArgsConstructor
public class EventArgumentMatcher<T> extends ArgumentMatcher<Event<T>> {
    private final T obj;

    @Override
    public boolean matches(Object argument) {
        return ((Event<T>) argument).getData().equals(obj);
    }
}
