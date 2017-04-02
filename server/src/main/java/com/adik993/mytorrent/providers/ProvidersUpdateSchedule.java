package com.adik993.mytorrent.providers;

import com.adik993.mytorrent.notification.EventContextFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProvidersUpdateSchedule {
    private final TorrentsProvidersFacade torrentsProvidersFacade;
    private final EventContextFactory eventContextFactory;

    @Scheduled(fixedDelayString = "${tc.providers.update-interval}")
    public void updateProviders() {
        torrentsProvidersFacade.updateUpStatus(eventContextFactory.createProviderUpdateContext());
    }
}
