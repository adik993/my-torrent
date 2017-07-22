package com.adik993.mytorrent.notification.receivers;

import com.adik993.mytorrent.notification.EventContextFactory;
import com.adik993.mytorrent.notification.Topic;
import com.adik993.mytorrent.providers.TorrentProvider;
import com.adik993.mytorrent.providers.TorrentProviderFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.bus.EventBus;

@Service
@Slf4j
public class ProviderSearchFailedReceiver extends Receiver<TorrentProvider> {

    private final TorrentProviderFacade torrentProviderFacade;
    private final EventContextFactory eventContextFactory;

    @Autowired
    public ProviderSearchFailedReceiver(EventBus eventBus, TorrentProviderFacade torrentProviderFacade, EventContextFactory eventContextFactory) {
        super(eventBus);
        this.torrentProviderFacade = torrentProviderFacade;
        this.eventContextFactory = eventContextFactory;
    }

    @Override
    protected void consume(TorrentProvider obj) {
        log.debug("Search failed on {}", obj);
        torrentProviderFacade.updateUpStatus(eventContextFactory.createProviderUpdateContext());
    }

    @Override
    protected String getTopic() {
        return Topic.PROVIDER_SEARCH_FAILED;
    }
}
