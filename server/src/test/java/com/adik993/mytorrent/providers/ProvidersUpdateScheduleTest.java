package com.adik993.mytorrent.providers;

import com.adik993.mytorrent.notification.EventContextFactory;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ProvidersUpdateScheduleTest {
    private ProvidersUpdateSchedule underTest;
    private TorrentsProvidersFacade torrentsProvidersFacade;
    private EventContextFactory eventContextFactory;

    @Before
    public void init() {
        torrentsProvidersFacade = mock(TorrentsProvidersFacade.class);
        eventContextFactory = mock(EventContextFactory.class);
        underTest = new ProvidersUpdateSchedule(torrentsProvidersFacade, eventContextFactory);
    }

    @Test
    public void updateProviders() throws Exception {
        underTest.updateProviders();
        verify(torrentsProvidersFacade, times(1)).updateUpStatus(any());
    }

}