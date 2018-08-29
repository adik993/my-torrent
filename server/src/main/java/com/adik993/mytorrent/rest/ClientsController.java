package com.adik993.mytorrent.rest;

import com.adik993.mytorrent.clients.TorrentClientDto;
import com.adik993.mytorrent.clients.TorrentClientFacade;
import io.reactivex.Flowable;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static io.reactivex.Flowable.fromIterable;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@Slf4j
@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientsController {
    private final TorrentClientFacade torrentClientFacade;

    @GetMapping(produces = TEXT_EVENT_STREAM_VALUE)
    public Flowable<List<TorrentClientDto>> providersStream() {
        return torrentClientFacade.clientUpdates()
                .flatMapSingle(torrentProviders -> fromIterable(torrentProviders)
                        .map(TorrentClientDto::new).toList())
                .doOnNext(dtos -> log.debug("sending updated clients: {}", dtos));
    }

    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    public Single<List<TorrentClientDto>> getProviders() {
        return torrentClientFacade.getProviders()
                .flatMap(torrentProviders -> fromIterable(torrentProviders)
                        .map(TorrentClientDto::new).toList());
    }
}
