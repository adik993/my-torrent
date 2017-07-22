package com.adik993.mytorrent.websocket;

import com.adik993.mytorrent.providers.TorrentProvider;
import com.adik993.mytorrent.providers.TorrentProviderDto;
import com.adik993.mytorrent.providers.TorrentProviderFacade;
import com.adik993.mytorrent.providers.TpbProvider;
import com.adik993.tpbclient.TpbClient;
import org.junit.Before;
import org.junit.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class WebSocketSenderTest {
    private WebSocketSender underTest;
    private TorrentProviderFacade torrentProviderFacade;
    private List<TorrentProvider> providers = Arrays.asList(
            new TpbProvider(TpbClient.withHost("aaa.com")),
            new TpbProvider(TpbClient.withHost("bbb.com"))
    );
    private SimpMessagingTemplate simpMessagingTemplate;

    @Before
    public void init() {
        simpMessagingTemplate = mock(SimpMessagingTemplate.class);
        torrentProviderFacade = mock(TorrentProviderFacade.class);
        when(torrentProviderFacade.getProviders()).thenReturn(providers);
        underTest = new WebSocketSender(torrentProviderFacade, simpMessagingTemplate);
    }

    @Test
    public void sendProviders() throws Exception {
        underTest.sendProviders();
        verify(simpMessagingTemplate, times(1)).convertAndSend("/client/providers", TorrentProviderDto.fromCollection(providers));
    }

}