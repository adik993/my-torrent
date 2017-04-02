package com.adik993.mytorrent.notification.receivers;

import com.adik993.mytorrent.notification.EventContextFactory;
import com.adik993.mytorrent.notification.contexts.ProviderUpdateContext;
import com.adik993.mytorrent.providers.TorrentsProvidersFacade;
import com.adik993.mytorrent.providers.TpbProvider;
import com.adik993.tpbclient.TpbClient;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.*;

public class ProviderSearchFailedReceiverTest {
    private ProviderSearchFailedReceiver underTest;
    private TorrentsProvidersFacade torrentsProvidersFacade;
    private EventContextFactory eventContextFactory;

    @Before
    public void init() {
        torrentsProvidersFacade = mock(TorrentsProvidersFacade.class);
        eventContextFactory = new EventContextFactory(null);

        underTest = new ProviderSearchFailedReceiver(null, torrentsProvidersFacade, eventContextFactory);
    }

    @Test
    public void consume() throws Exception {
        underTest.consume(new TpbProvider(TpbClient.withHost("aaa.com")));
        verify(torrentsProvidersFacade, times(1)).updateUpStatus(notNull(ProviderUpdateContext.class));
    }

}