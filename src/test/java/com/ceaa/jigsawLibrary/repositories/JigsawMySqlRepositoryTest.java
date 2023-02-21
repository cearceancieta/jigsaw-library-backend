package com.ceaa.jigsawLibrary.repositories;

import com.ceaa.jigsawLibrary.jigsaw.Jigsaw;
import com.ceaa.jigsawLibrary.jigsaw.JigsawNotFoundException;
import com.ceaa.jigsawLibrary.jigsaw.JigsawRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JigsawMySqlRepositoryTest {

    @Mock
    JigsawMySqlDataSource dataSource;
    private JigsawRepository repository;

    @BeforeEach
    void setUp() {
        repository = new JigsawMySqlRepository(dataSource);
    }

    @Test
    void getJigsaw_passingANullJigsawId() {
        UUID nullJigsawId = null;
        IllegalArgumentException sourceException = new IllegalArgumentException("error message");
        when(dataSource.findById(nullJigsawId)).thenThrow(sourceException);

        Exception thrownException = assertThrows(IllegalArgumentException.class,
                () -> repository.get(nullJigsawId));
        assertEquals(sourceException.getMessage(), thrownException.getMessage());
    }

    @Test
    void getJigsaw_searchingForAJigsawThatDoesNotExist() {
        UUID nonExistingJigsawId = UUID.randomUUID();
        when(dataSource.findById(nonExistingJigsawId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(JigsawNotFoundException.class,
                () -> repository.get(nonExistingJigsawId));
        assertEquals("Jigsaw with Id " + nonExistingJigsawId + " was not found",
                exception.getMessage());
    }

    @Test
    void getJigsaw_searchForExistingJigsaw() {
        JigsawEntity storedJigsaw = new JigsawEntity(
                UUID.randomUUID(),
                "jigsaw title",
                "jigsaw brand",
                1500
        );
        when(dataSource.findById(storedJigsaw.getId())).thenReturn(Optional.of(storedJigsaw));

        Jigsaw foundJigsaw = repository.get(storedJigsaw.getId());

        assertEquals(storedJigsaw.getId(), foundJigsaw.getId());
        assertEquals(storedJigsaw.getTitle(), foundJigsaw.getTitle());
        assertEquals(storedJigsaw.getBrand(), foundJigsaw.getBrand());
        assertEquals(storedJigsaw.getNPieces(), foundJigsaw.getNPieces());
    }

}
