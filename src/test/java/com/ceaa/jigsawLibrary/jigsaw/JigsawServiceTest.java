package com.ceaa.jigsawLibrary.jigsaw;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JigsawServiceTest {

    @Mock JigsawRepository repository;
    private JigsawService jigsawService;

    @BeforeEach
    void setUp() {
        jigsawService = new JigsawService(repository);
    }

    @Test
    void tryingToGetOneJigsawButJigsawDoesNotExist() {
        String lookedForId = "id";

        when(repository.get(lookedForId)).thenThrow(new JigsawNotFoundException(lookedForId));

        assertThrows(JigsawNotFoundException.class,
                () -> jigsawService.getJigsaw(lookedForId));
    }

    @Test
    void getExistingJigsaw() {
        Jigsaw storedJigsaw = Jigsaw.builder()
                .id("12")
                .title("Jigsaw Title")
                .brand("Brand")
                .nPieces(1500)
                .build();

        when(repository.get(storedJigsaw.getId())).thenReturn(storedJigsaw);

        Jigsaw retrievedJigsaw = jigsawService.getJigsaw(storedJigsaw.getId());

        assertNotNull(retrievedJigsaw);
        assertEquals(storedJigsaw, retrievedJigsaw);
    }

    @Test
    void gettingJigsawListButItsEmpty() {
        when(repository.find()).thenReturn(List.of());

        assertThat(jigsawService.getJigsaws()).isNotNull().isEmpty();
    }

    @Test
    void findAllJigsaws() {
        List<Jigsaw> storedJigsaws = Arrays.asList(
                Jigsaw.builder().id("1").title("title1").brand("brand1").nPieces(500).build(),
                Jigsaw.builder().id("2").title("title2").brand("brand2").nPieces(1000).build(),
                Jigsaw.builder().id("3").title("title3").brand("brand3").nPieces(1500).build()
        );
        when(repository.find()).thenReturn(storedJigsaws);

        assertThat(jigsawService.getJigsaws())
                .isNotNull()
                .isNotEmpty()
                .hasSameSizeAs(storedJigsaws)
                .hasSameElementsAs(storedJigsaws);
    }

    @Test
    void storingNewJigsaw() {
        Jigsaw newJigsaw = Jigsaw.builder().title("title").brand("brand").shape("shape").nPieces(500).build();
        Jigsaw createdJigsaw = Jigsaw.builder().id("newId")
                .title(newJigsaw.getTitle()).brand(newJigsaw.getBrand()).shape(newJigsaw.getShape())
                .nPieces(newJigsaw.getNPieces()).build();
        when(repository.save(newJigsaw)).thenReturn(createdJigsaw);

        Jigsaw returnedJigsaw = jigsawService.saveJigsaw(newJigsaw);

        verify(repository, times(1)).save(newJigsaw);
        assertThat(returnedJigsaw).isEqualTo(createdJigsaw);
    }

}
