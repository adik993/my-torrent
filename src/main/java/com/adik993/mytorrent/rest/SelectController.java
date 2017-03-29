package com.adik993.mytorrent.rest;

import com.adik993.mytorrent.services.SelectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/select")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SelectController {
    private final SelectService selectService;

    @PostMapping
    public ResponseEntity<Void> select(@Param("id") Long id) {
        try {
            selectService.select(id);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
