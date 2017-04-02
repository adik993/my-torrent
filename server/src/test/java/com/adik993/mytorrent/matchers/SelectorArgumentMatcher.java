package com.adik993.mytorrent.matchers;

import lombok.RequiredArgsConstructor;
import org.mockito.ArgumentMatcher;
import reactor.bus.selector.Selector;

@SuppressWarnings("unchecked")
@RequiredArgsConstructor
public class SelectorArgumentMatcher<T> extends ArgumentMatcher<Selector<T>> {
    private final T obj;

    @Override
    public boolean matches(Object argument) {
        return ((Selector<T>) argument).getObject().equals(obj);
    }
}
