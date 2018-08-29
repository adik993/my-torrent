package com.adik993.mytorrent.rest;

import com.adik993.mytorrent.clients.TorrentClient;
import com.adik993.mytorrent.clients.TorrentClientFacade;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final TorrentClientFacade torrentClientFacade;

    @GetMapping("/refresh-clients-list")
    public Single<Collection<TorrentClient>> refreshProviders() {
        return torrentClientFacade.refreshClientsList();
    }
}
