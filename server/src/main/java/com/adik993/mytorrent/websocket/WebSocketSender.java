package com.adik993.mytorrent.websocket;

import com.adik993.mytorrent.providers.TorrentsProviderDto;
import com.adik993.mytorrent.providers.TorrentsProvidersFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class WebSocketSender {
    private final TorrentsProvidersFacade torrentsProvidersFacade;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendProviders() {
        log.debug("Sending providers through WebSocket");
        List<TorrentsProviderDto> dtos = TorrentsProviderDto.fromCollection(torrentsProvidersFacade.getProviders());
        simpMessagingTemplate.convertAndSend("/client/providers", dtos);
    }
}
