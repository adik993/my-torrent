package com.adik993.mytorrent.providers;

import com.adik993.mytorrent.notification.EventContextFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ConditionalOnExpression("${tc.providers.update-interval}>0")
@Slf4j
public class ProvidersUpdateSchedule {
    private final TorrentsProvidersFacade torrentsProvidersFacade;
    private final EventContextFactory eventContextFactory;

    @Scheduled(fixedDelayString = "#{${tc.providers.update-interval}}")
    public void updateProviders() {
        log.debug("Running scheduled providers update");
        torrentsProvidersFacade.updateUpStatus(eventContextFactory.createProviderUpdateContext());
    }
}
