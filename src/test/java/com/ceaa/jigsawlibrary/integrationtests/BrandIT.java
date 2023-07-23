package com.ceaa.jigsawlibrary.integrationtests;

import com.ceaa.jigsawlibrary.brand.repositories.BrandDocument;
import com.ceaa.jigsawlibrary.brand.repositories.BrandMongoDataSource;
import com.ceaa.jigsawlibrary.jigsaw.controllers.BrandDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BrandIT {

    @Autowired
    private BrandMongoDataSource dataSource;

    @LocalServerPort
    private int port;

    WebTestClient client;
    private ModelMapper mapper;

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port + "/brands").build();
        mapper = new ModelMapper();
    }

    @AfterEach
    void tearDown() {
        dataSource.deleteAll().block();
    }

    @Test
    void whenGettingBrandsAndNoneAreStoredShouldReturnEmptyList() {
        client.get().exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(BrandDto.class)
                .hasSize(0);
    }

    @Test
    void getAllBrands() {
        List<BrandDto> storedBrands = Flux.range(1, 3)
                .map(i -> BrandDocument.builder().name("brand" + i).build())
                .flatMap(dataSource::save)
                .map(document -> mapper.map(document, BrandDto.class))
                .collectList()
                .block();

        client.get().exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(BrandDto.class)
                .consumeWith(result -> {
                    assertThat(result.getResponseBody()).isNotNull();
                    assertThat(result.getResponseBody()).hasSameElementsAs(storedBrands);
                    assertThat(result.getResponseBody()).containsAll(storedBrands);
                });
    }

}
