package com.ceaa.jigsawlibrary.brand.controllers;

import com.ceaa.jigsawlibrary.brand.domain.BrandService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping(value = "/brands", produces = MediaType.APPLICATION_JSON_VALUE)
public class BrandController {

    private final BrandService service;
    private final ModelMapper mapper;

    public BrandController(BrandService service, ModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<BrandDto> getAll() {
        log.info("Get all brands");
        return service.getBrands()
                .map(brand -> mapper.map(brand, BrandDto.class)).log();
    }
}
