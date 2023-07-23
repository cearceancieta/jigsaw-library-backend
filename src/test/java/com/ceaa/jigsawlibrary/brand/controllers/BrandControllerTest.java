package com.ceaa.jigsawlibrary.brand.controllers;

import com.ceaa.jigsawlibrary.brand.domain.Brand;
import com.ceaa.jigsawlibrary.brand.domain.BrandService;
import com.ceaa.jigsawlibrary.jigsaw.controllers.BrandDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BrandControllerTest {

    @Mock
    private BrandService service;
    private BrandController controller;
    private ModelMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ModelMapper();
        controller = new BrandController(service, mapper);
    }

    @Test
    void getAll_emptyList() {
        when(service.getBrands()).thenReturn(Flux.empty());

        StepVerifier.create(controller.getAll())
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void getAll_brandsFound() {
        List<Brand> storedBrands = List.of(
                Brand.builder().id("id1").name("name1").build(),
                Brand.builder().id("id2").name("name2").build(),
                Brand.builder().id("id3").name("name3").build()
        );
        when(service.getBrands()).thenReturn(Flux.fromIterable(storedBrands));

        StepVerifier.create(controller.getAll())
                .expectNextSequence(storedBrands.stream()
                        .map(brand -> mapper.map(brand, BrandDto.class))
                        .collect(Collectors.toList()))
                .verifyComplete();
    }

}
