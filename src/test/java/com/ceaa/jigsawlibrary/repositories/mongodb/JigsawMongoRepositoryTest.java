package com.ceaa.jigsawlibrary.repositories.mongodb;

import com.ceaa.jigsawlibrary.jigsaw.Jigsaw;
import com.ceaa.jigsawlibrary.jigsaw.JigsawRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JigsawMongoRepositoryTest {

    @Mock
    JigsawMongoDataSource reactiveDataSource;

    private final JigsawDocumentMapper mapper = new JigsawDocumentMapper();
    private JigsawRepository repository;

    @BeforeEach
    void setup() {
        repository = new JigsawMongoRepository(reactiveDataSource, mapper);
    }

    @Test
    void get_searchingForAJigsawThatDoesNotExist() {
        String nonExistingJigsawId = "nonExistingId";
        when(reactiveDataSource.findById(nonExistingJigsawId)).thenReturn(Mono.empty());

        Mono<Jigsaw> foundJigsaw = repository.get(nonExistingJigsawId);

        StepVerifier.create(foundJigsaw)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void get_searchForExistingJigsaw() {
        JigsawDocument storedJigsaw = JigsawDocument.builder()
                .id("id").title("title").brand("brand").shape("shape").nPieces(1000)
                .build();
        when(reactiveDataSource.findById(storedJigsaw.getId())).thenReturn(Mono.just(storedJigsaw));

        Mono<Jigsaw> foundJigsaw = repository.get(storedJigsaw.getId());

        StepVerifier.create(foundJigsaw)
                .expectNext(mapper.mapFromDocument(storedJigsaw))
                .verifyComplete();
    }

    @Test
    void find_searchForJigsawsButNoneAreStored() {
        when(reactiveDataSource.findAll()).thenReturn(Flux.empty());

        Flux<Jigsaw> foundJigsaws = repository.find();

        StepVerifier.create(foundJigsaws)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void find_searchForAllJigsaws() {
        List<JigsawDocument> storedJigsaws = List.of(
                JigsawDocument.builder().title("title1").brand("brand1").shape("shape1").nPieces(500).build(),
                JigsawDocument.builder().title("title2").brand("brand2").shape("shape2").nPieces(1000).build(),
                JigsawDocument.builder().title("title3").brand("brand3").shape("shape3").nPieces(1500).build());
        when(reactiveDataSource.findAll()).thenReturn(Flux.fromIterable(storedJigsaws));

        Flux<Jigsaw> foundJigsaws = repository.find();

        StepVerifier.create(foundJigsaws)
                .expectNextSequence(mapper.mapFromDocumentList(storedJigsaws))
                .verifyComplete();
    }

    @Test
    void save_createNewJigsaw() {
        Jigsaw newJigsaw = Jigsaw.builder()
                .title("title").brand("brand").shape("shape").nPieces(500)
                .build();
        JigsawDocument createdJigsaw = JigsawDocument.builder().id("newId")
                .title(newJigsaw.getTitle()).brand(newJigsaw.getBrand()).shape(newJigsaw.getShape())
                .nPieces(newJigsaw.getNPieces()).build();
        when(reactiveDataSource.save(any())).thenReturn(Mono.just(createdJigsaw));

        StepVerifier.create(repository.save(newJigsaw))
                .expectNext(mapper.mapFromDocument(createdJigsaw))
                .verifyComplete();

        ArgumentCaptor<JigsawDocument> captor = ArgumentCaptor.forClass(JigsawDocument.class);
        verify(reactiveDataSource, times(1)).save(captor.capture());
        assertThat(mapper.mapFromDocument(captor.getValue())).isEqualTo(newJigsaw);
    }

}
