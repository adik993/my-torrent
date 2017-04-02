package com.adik993.mytorrent.notification.receivers;

import com.adik993.mytorrent.notification.EventContextFactory;
import com.adik993.mytorrent.notification.Topic;
import com.adik993.mytorrent.providers.TorrentsProvider;
import com.adik993.mytorrent.providers.TorrentsProvidersFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.bus.EventBus;

@Service
@Slf4j
public class ProviderSearchFailedReceiver extends Receiver<TorrentsProvider> {

    private final TorrentsProvidersFacade torrentsProvidersFacade;
    private final EventContextFactory eventContextFactory;

    @Autowired
    public ProviderSearchFailedReceiver(EventBus eventBus, TorrentsProvidersFacade torrentsProvidersFacade, EventContextFactory eventContextFactory) {
        super(eventBus);
        this.torrentsProvidersFacade = torrentsProvidersFacade;
        this.eventContextFactory = eventContextFactory;
    }

    @Override
    protected void consume(TorrentsProvider obj) {
        log.debug("Search failed on {}", obj);
        torrentsProvidersFacade.updateUpStatus(eventContextFactory.createProviderUpdateContext());
    }

    @Override
    protected String getTopic() {
        return Topic.PROVIDER_SEARCH_FAILED;
    }
}
