package com.adik993.mytorrent.websocket;

import com.adik993.mytorrent.providers.TorrentsProvider;
import com.adik993.mytorrent.providers.TorrentsProviderDto;
import com.adik993.mytorrent.providers.TorrentsProvidersFacade;
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
    private TorrentsProvidersFacade torrentsProvidersFacade;
    private List<TorrentsProvider> providers = Arrays.asList(
            new TpbProvider(TpbClient.withHost("aaa.com")),
            new TpbProvider(TpbClient.withHost("bbb.com"))
    );
    private SimpMessagingTemplate simpMessagingTemplate;

    @Before
    public void init() {
        simpMessagingTemplate = mock(SimpMessagingTemplate.class);
        torrentsProvidersFacade = mock(TorrentsProvidersFacade.class);
        when(torrentsProvidersFacade.getProviders()).thenReturn(providers);
        underTest = new WebSocketSender(torrentsProvidersFacade, simpMessagingTemplate);
    }

    @Test
    public void sendProviders() throws Exception {
        underTest.sendProviders();
        verify(simpMessagingTemplate, times(1)).convertAndSend("/client/providers", TorrentsProviderDto.fromCollection(providers));
    }

}