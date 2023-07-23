package com.ceaa.jigsawlibrary.brand.repositories;

import com.ceaa.jigsawlibrary.brand.domain.Brand;
import com.ceaa.jigsawlibrary.brand.domain.BrandRepository;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BrandMongoRepositoryTest {

    @Mock BrandMongoDataSource dataSource;
    private BrandRepository repository;
    private ModelMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ModelMapper();
        repository = new BrandMongoRepository(dataSource, mapper);
    }

    @Test
    void find_searchForBrandsButNoneAreStored() {
        when(dataSource.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(repository.find())
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void find_searchForAllBrands() {
        List<BrandDocument> storedBrands = List.of(
                BrandDocument.builder().id("id1").name("name1").build(),
                BrandDocument.builder().id("id2").name("name2").build(),
                BrandDocument.builder().id("id3").name("name3").build()
        );
        when(dataSource.findAll()).thenReturn(Flux.fromIterable(storedBrands));

        StepVerifier.create(repository.find())
                .expectNextSequence(storedBrands.stream()
                        .map(document -> mapper.map(document, Brand.class))
                        .collect(Collectors.toList()))
                .verifyComplete();
    }

}
