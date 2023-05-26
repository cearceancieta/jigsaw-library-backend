package com.ceaa.jigsawlibrary.repositories.mongodb;

import com.ceaa.jigsawlibrary.jigsaw.Jigsaw;
import com.ceaa.jigsawlibrary.jigsaw.JigsawNotFoundException;
import com.ceaa.jigsawlibrary.jigsaw.JigsawRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JigsawMongoRepositoryTest {

    @Mock
    JigsawMongoDataSource dataSource;
    private final JigsawDocumentMapper mapper = new JigsawDocumentMapper();
    private JigsawRepository repository;

    @BeforeEach
    void setup() {
        repository = new JigsawMongoRepository(dataSource, mapper);
    }

    @Test
    void get_searchingForAJigsawThatDoesNotExist() {
        String nonExistingJigsawId = "nonExistingId";
        when(dataSource.findById(nonExistingJigsawId)).thenReturn(Optional.empty());

        assertThrows(JigsawNotFoundException.class, () -> repository.get(nonExistingJigsawId));
    }

    @Test
    void get_searchForExistingJigsaw() {
        JigsawDocument storedJigsaw = JigsawDocument.builder()
                .id("id").title("title").brand("brand").shape("shape").nPieces(1000)
                .build();
        when(dataSource.findById(storedJigsaw.getId())).thenReturn(Optional.of(storedJigsaw));

        Jigsaw foundJigsaw = repository.get(storedJigsaw.getId());

        assertThat(foundJigsaw).isEqualTo(mapper.mapFromDocument(storedJigsaw));
    }

    @Test
    void find_searchForJigsawsButNoneAreStored() {
        when(dataSource.findAll()).thenReturn(List.of());
        assertThat(repository.find()).isNotNull().isEmpty();
    }

    @Test
    void find_searchForAllJigsaws() {
        List<JigsawDocument> storedJigsaws = List.of(
                JigsawDocument.builder().title("title1").brand("brand1").shape("shape1").nPieces(500).build(),
                JigsawDocument.builder().title("title2").brand("brand2").shape("shape2").nPieces(1000).build(),
                JigsawDocument.builder().title("title3").brand("brand3").shape("shape3").nPieces(1500).build());
        when(dataSource.findAll()).thenReturn(storedJigsaws);

        List<Jigsaw> foundJigsaws = repository.find();

        assertThat(foundJigsaws).isNotNull().isNotEmpty().hasSameSizeAs(storedJigsaws)
                .containsAll(storedJigsaws.stream()
                .map(mapper::mapFromDocument)
                .toList());
    }

    @Test
    void save_createNewJigsaw() {
        Jigsaw newJigsaw = Jigsaw.builder()
                .title("title").brand("brand").shape("shape").nPieces(500)
                .build();
        JigsawDocument createdJigsaw = JigsawDocument.builder().id("newId")
                .title(newJigsaw.getTitle()).brand(newJigsaw.getBrand()).shape(newJigsaw.getShape())
                .nPieces(newJigsaw.getNPieces()).build();
        when(dataSource.save(any())).thenReturn(createdJigsaw);

        Jigsaw returnedJigsaw = repository.save(newJigsaw);

        ArgumentCaptor<JigsawDocument> captor = ArgumentCaptor.forClass(JigsawDocument.class);
        verify(dataSource, times(1)).save(captor.capture());
        assertThat(mapper.mapFromDocument(captor.getValue())).isEqualTo(newJigsaw);

        assertThat(returnedJigsaw).isEqualTo(mapper.mapFromDocument(createdJigsaw));
    }

}
