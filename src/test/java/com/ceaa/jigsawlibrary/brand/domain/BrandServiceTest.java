package com.ceaa.jigsawlibrary.brand.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BrandServiceTest {

    @Mock BrandRepository repository;
    private BrandServiceImp service;

    @BeforeEach
    void setUp() {
        service = new BrandServiceImp(repository);
    }

    @Test
    void tryingToGetBrandListButItsEmpty() {
        when(repository.find()).thenReturn(Flux.empty());

        StepVerifier.create(service.getBrands())
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void findAllBrands() {
        List<Brand> storedBrands = List.of(
                Brand.builder().id("id1").name("name1").build(),
                Brand.builder().id("id2").name("name2").build(),
                Brand.builder().id("id3").name("name3").build()
        );
        when(repository.find()).thenReturn(Flux.fromIterable(storedBrands));

        StepVerifier.create(service.getBrands())
                .expectNextSequence(storedBrands)
                .verifyComplete();
    }

}
