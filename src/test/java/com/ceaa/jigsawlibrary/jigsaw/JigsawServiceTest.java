package com.ceaa.jigsawlibrary.jigsaw;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JigsawServiceTest {

    @Mock JigsawRepository repository;
    private JigsawService jigsawService;

    @BeforeEach
    void setUp() {
        jigsawService = new JigsawService(repository);
    }

    @Test
    void tryingToGetOneJigsawButJigsawDoesNotExist() {
        String lookedForId = "id";

        when(repository.get(lookedForId)).thenReturn(Mono.empty());

        Mono<Jigsaw> retrieved = jigsawService.getJigsaw(lookedForId);

        StepVerifier.create(retrieved)
                .expectError(JigsawNotFoundException.class)
                .verify();
    }

    @Test
    void getExistingJigsaw() {
        Jigsaw storedJigsaw = Jigsaw.builder()
                .id("12")
                .title("Jigsaw Title")
                .brand("Brand")
                .nPieces(1500)
                .build();

        when(repository.get(storedJigsaw.getId())).thenReturn(Mono.just(storedJigsaw));

        Mono<Jigsaw> retrievedJigsaw = jigsawService.getJigsaw(storedJigsaw.getId());

        StepVerifier.create(retrievedJigsaw)
                        .consumeNextWith(jigsaw -> {
                            assertNotNull(jigsaw);
                            assertEquals(storedJigsaw, jigsaw);
                        })
                .verifyComplete();
    }

    @Test
    void gettingJigsawListButItsEmpty() {
        when(repository.find()).thenReturn(Flux.empty());

        StepVerifier.create(jigsawService.getJigsaws())
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void findAllJigsaws() {
        List<Jigsaw> storedJigsaws = Arrays.asList(
                Jigsaw.builder().id("1").title("title1").brand("brand1").nPieces(500).build(),
                Jigsaw.builder().id("2").title("title2").brand("brand2").nPieces(1000).build(),
                Jigsaw.builder().id("3").title("title3").brand("brand3").nPieces(1500).build()
        );
        when(repository.find()).thenReturn(Flux.fromIterable(storedJigsaws));

        StepVerifier.create(jigsawService.getJigsaws())
                .expectNextSequence(storedJigsaws)
                .verifyComplete();
    }

    @Test
    void storingNewJigsaw() {
        Jigsaw newJigsaw = Jigsaw.builder().title("title").brand("brand").shape("shape").nPieces(500).build();
        Jigsaw createdJigsaw = Jigsaw.builder().id("newId")
                .title(newJigsaw.getTitle()).brand(newJigsaw.getBrand()).shape(newJigsaw.getShape())
                .nPieces(newJigsaw.getNPieces()).build();
        when(repository.save(newJigsaw)).thenReturn(Mono.just(createdJigsaw));

        StepVerifier.create(jigsawService.saveJigsaw(newJigsaw))
                .expectNext(createdJigsaw)
                .verifyComplete();

        verify(repository, times(1)).save(newJigsaw);
    }

}
