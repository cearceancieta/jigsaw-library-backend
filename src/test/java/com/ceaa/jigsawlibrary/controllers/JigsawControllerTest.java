package com.ceaa.jigsawlibrary.controllers;

import com.ceaa.jigsawlibrary.jigsaw.Jigsaw;
import com.ceaa.jigsawlibrary.jigsaw.JigsawNotFoundException;
import com.ceaa.jigsawlibrary.jigsaw.JigsawService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JigsawControllerTest {

    @Mock
    private JigsawService service;
    private JigsawController controller;

    @BeforeEach
    void setUp() {
        controller = new JigsawController(service);
    }

    @Test
    void getOne_jigsawNotFound() {
        String id = "id";
        JigsawNotFoundException exceptionThrownByService = new JigsawNotFoundException(id);
        when(service.getJigsaw(id)).thenReturn(Mono.error(exceptionThrownByService));

        Mono<ResponseEntity<Jigsaw>> responseMono = controller.getOne(id);

        StepVerifier.create(responseMono)
                .expectErrorSatisfies(throwable -> {
                    assertEquals(exceptionThrownByService, throwable);
                })
                .verify();
    }

    @Test
    void getOne_jigsawFound() {
        Jigsaw storedJigsaw = Jigsaw.builder()
                .id("id")
                .title("Jigsaw title")
                .brand("Jigsaw brand")
                .nPieces(500)
                .build();
        when(service.getJigsaw(storedJigsaw.getId())).thenReturn(Mono.just(storedJigsaw));

        Mono<ResponseEntity<Jigsaw>> responseMono = controller.getOne(storedJigsaw.getId());

        StepVerifier.create(responseMono)
                .consumeNextWith(response -> {
                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                    assertThat(response.getBody()).isEqualTo(storedJigsaw);
                })
                .verifyComplete();
    }

    @Test
    void getAll_emptyList() {
        when(service.getJigsaws()).thenReturn(Flux.empty());

        StepVerifier.create(controller.getAll())
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void getAll_jigsawsFound() {
        List<Jigsaw> storedJigsaws = List.of(
                Jigsaw.builder().id("id1").title("title1").brand("brand1").nPieces(500).build(),
                Jigsaw.builder().id("id2").title("title2").brand("brand2").nPieces(1000).build(),
                Jigsaw.builder().id("id3").title("title3").brand("brand3").nPieces(1500).build()
        );
        when(service.getJigsaws()).thenReturn(Flux.fromIterable(storedJigsaws));

        StepVerifier.create(controller.getAll())
                .expectNextSequence(storedJigsaws)
                .verifyComplete();
    }

    @Test
    void save_saveNewJigsaw() {
        Jigsaw newJigsaw = Jigsaw.builder()
                .title("title").brand("brand").shape("shape").nPieces(500)
                .build();
        Jigsaw createdJigsaw = Jigsaw.builder().id("createdId")
                .title(newJigsaw.getTitle()).brand(newJigsaw.getBrand()).shape(newJigsaw.getShape())
                .nPieces(newJigsaw.getNPieces()).build();
        when(service.saveJigsaw(newJigsaw)).thenReturn(Mono.just(createdJigsaw));

        StepVerifier.create(controller.save(newJigsaw))
                .consumeNextWith(response -> {
                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
                    assertThat(response.getHeaders().getLocation()).isNotNull();
                    assertThat(response.getHeaders().getLocation().toString()).endsWith(createdJigsaw.getId());
                    assertThat(response.getBody()).isNotNull();
                    assertThat(response.getBody()).isEqualTo(createdJigsaw);
                })
                .verifyComplete();

        verify(service, times(1)).saveJigsaw(newJigsaw);
    }
}
