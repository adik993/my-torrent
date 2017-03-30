package com.adik993.mytorrent.rest;

import com.adik993.mytorrent.services.SelectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/result")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SearchResultController {
    private final SelectService selectService;

    @PostMapping("/select")
    public ResponseEntity<Void> select(@RequestParam("id") Long id, @RequestParam("selected") boolean selected) {
        try {
            selectService.select(id, selected);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
