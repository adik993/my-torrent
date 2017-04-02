package com.adik993.mytorrent.rest;

import com.adik993.mytorrent.providers.TorrentsProviderDto;
import com.adik993.mytorrent.providers.TorrentsProvidersFacade;
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
    private final TorrentsProvidersFacade torrentsProvidersFacade;

    @GetMapping
    public List<TorrentsProviderDto> getProviders() {
        return TorrentsProviderDto.fromCollection(torrentsProvidersFacade.getProviders());
    }
}
