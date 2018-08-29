package com.adik993.mytorrent.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PACKAGE;

@RequiredArgsConstructor(access = PACKAGE)
class Event<T> {
    private final String topic;
    @Getter
    private final T data;

    boolean matchesTopic(String topic) {
        return topic.equals(this.topic);
    }

    boolean matchesClass(Class<?> clazz) {
        return clazz.isAssignableFrom(this.getData().getClass());
    }
}
