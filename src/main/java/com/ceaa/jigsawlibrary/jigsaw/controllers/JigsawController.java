package com.ceaa.jigsawlibrary.jigsaw.controllers;

import com.ceaa.jigsawlibrary.jigsaw.domain.Jigsaw;
import com.ceaa.jigsawlibrary.jigsaw.domain.JigsawService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping(value = "/jigsaws", produces = "application/json")
@Validated
public class JigsawController {

    private final JigsawService service;
    private final ModelMapper mapper;

    public JigsawController(JigsawService service, ModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<JigsawDto>> getOne(@PathVariable @Pattern(regexp = "[a-zA-Z0-9]+",
            message = "must be alphanumerical") String id) {
        log.info("Get jigsaw with id {}", id);
        return service.getJigsaw(id).log()
                .map(this::mapToDto)
                .map(ResponseEntity::ok);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<JigsawDto> getAll() {
        log.info("Get all jigsaws");
        return service.getJigsaws().log()
                .map(this::mapToDto);
    }

    @PostMapping(consumes = "application/json")
    public Mono<ResponseEntity<JigsawDto>> create(@Valid @RequestBody SaveJigsawRequestDto jigsawRequest) {
        log.info("Saving " + jigsawRequest.toString());
        return service.saveJigsaw(mapToJigsaw(jigsawRequest)).log()
                .map(this::mapToDto)
                .map(createdJigsaw -> ResponseEntity
                        .created(URI.create("/jigsaws/" + createdJigsaw.getId()))
                        .body(createdJigsaw));
    }

    private JigsawDto mapToDto(Jigsaw jigsaw) {
        return mapper.map(jigsaw, JigsawDto.class);
    }

    private Jigsaw mapToJigsaw(SaveJigsawRequestDto dto) {
        return Jigsaw.builder()
                .title(dto.getTitle())
                .subtitle(dto.getSubtitle())
                .collection(dto.getCollection())
                .brand(dto.getBrand())
                .shape(dto.getShape())
                .nPieces(dto.getNPieces())
                .build();
    }
}
