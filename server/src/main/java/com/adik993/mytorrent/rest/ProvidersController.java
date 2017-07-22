package com.adik993.mytorrent.rest;

import com.adik993.mytorrent.providers.TorrentProviderDto;
import com.adik993.mytorrent.providers.TorrentProviderFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/providers")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProvidersController {
    private final TorrentProviderFacade torrentProviderFacade;

    @GetMapping
    public List<TorrentProviderDto> getProviders() {
        return TorrentProviderDto.fromCollection(torrentProviderFacade.getProviders());
    }
}
