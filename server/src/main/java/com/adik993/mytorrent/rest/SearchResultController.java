package com.adik993.mytorrent.rest;

import com.adik993.mytorrent.services.SelectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/result")
@RequiredArgsConstructor
public class SearchResultController {
    private final SelectService selectService;

    @PostMapping("/select")
    public Mono<ResponseEntity<Object>> select(@RequestParam("id") String id,
                                               @RequestParam("selected") boolean selected) {
        return selectService.select(id, selected)
                .map(search -> ResponseEntity.noContent().build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
