package com.ceaa.jigsawLibrary.controllers;

import com.ceaa.jigsawLibrary.jigsaw.Jigsaw;
import com.ceaa.jigsawLibrary.jigsaw.JigsawService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping(value = "/jigsaws", produces = "application/json")
public class JigsawController {

    private final JigsawService service;

    public JigsawController(JigsawService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jigsaw> getOne(@PathVariable String id) {
        log.info("Get jigsaw with id {}", id);
        return ResponseEntity.ok(service.getJigsaw(id));
    }

    @GetMapping
    public ResponseEntity<List<Jigsaw>> getAll() {
        log.info("Get all jigsaws");
        List<Jigsaw> jigsaws = service.getJigsaws();
        return ResponseEntity.ok(jigsaws);
    }

    @PostMapping
    public ResponseEntity<Jigsaw> save(@Valid @RequestBody Jigsaw jigsaw) {
        log.info("Saving " + jigsaw.toString());
        Jigsaw createdJigsaw = service.saveJigsaw(jigsaw);

        URI location = linkTo(methodOn(JigsawController.class).getOne(createdJigsaw.getId())).toUri();
        return ResponseEntity.created(location)
                .body(createdJigsaw);
    }
}
