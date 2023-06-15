package com.ceaa.jigsawlibrary.controllers;

import com.ceaa.jigsawlibrary.jigsaw.Jigsaw;
import com.ceaa.jigsawlibrary.jigsaw.JigsawService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping(value = "/jigsaws", produces = "application/json")
@Validated
public class JigsawController {

    private final JigsawService service;

    public JigsawController(JigsawService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Jigsaw>> getOne(@PathVariable @Pattern(regexp = "[a-zA-Z0-9]+",
            message = "must be alphanumerical") String id) {
        log.info("Get jigsaw with id {}", id);
        return service.getJigsaw(id).log()
                .map(ResponseEntity::ok);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<Jigsaw> getAll() {
        log.info("Get all jigsaws");
        return service.getJigsaws().log();
    }

    @PostMapping(consumes = "application/json")
    public Mono<ResponseEntity<Jigsaw>> save(@Valid @RequestBody Jigsaw jigsaw) {
        log.info("Saving " + jigsaw.toString());
        return service.saveJigsaw(jigsaw).log()
                .map(createdJigsaw -> ResponseEntity
                        .created(URI.create("/jigsaws/" + createdJigsaw.getId()))
                        .body(createdJigsaw));
    }
}
