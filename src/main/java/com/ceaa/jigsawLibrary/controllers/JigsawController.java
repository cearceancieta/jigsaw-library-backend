package com.ceaa.jigsawLibrary.controllers;

import com.ceaa.jigsawLibrary.jigsaw.Jigsaw;
import com.ceaa.jigsawLibrary.jigsaw.JigsawService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
public class JigsawController {

    private final JigsawService service;

    public JigsawController(JigsawService service) {
        this.service = service;
    }

    @GetMapping("/jigsaws/{id}")
    EntityModel<Jigsaw> getOne(@PathVariable UUID id) {
        log.info("Get jigsaw with id {}", id);
        return EntityModel.of(service.getJigsaw(id),
                linkTo(methodOn(JigsawController.class).getOne(id)).withSelfRel());
    }
}
