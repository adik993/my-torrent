package com.adik993.mytorrent.config;

import com.adik993.mytorrent.notification.EventBus;
import com.adik993.mytorrent.notification.EventContextFactory;
import io.reactivex.processors.PublishProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReactorConfig {

    @Bean
    EventBus eventBus() {
        return new EventBus(PublishProcessor.create());
    }

    @Bean
    EventContextFactory eventContextFactory(EventBus eventBus) {
        return new EventContextFactory(eventBus);
    }
}
