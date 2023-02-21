package com.ceaa.jigsawLibrary.jigsaw;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JigsawServiceTest {

    @Mock JigsawRepository repository;
    private JigsawService jigsawService;

    @BeforeEach
    void setUp() {
        jigsawService = new JigsawService(repository);
    }

    @Test
    void jigsawDoesNotExist() {
        UUID lookedForId = UUID.randomUUID();

        when(repository.get(lookedForId)).thenThrow(new JigsawNotFoundException(lookedForId));

        assertThrows(JigsawNotFoundException.class,
                () -> jigsawService.getJigsaw(lookedForId));
    }

    @Test
    void getExistingJigsaw() {
        Jigsaw storedJigsaw = Jigsaw.builder()
                .id(UUID.randomUUID())
                .title("Jigsaw Title")
                .brand("Brand")
                .nPieces(1500)
                .build();

        when(repository.get(storedJigsaw.getId())).thenReturn(storedJigsaw);

        Jigsaw retrievedJigsaw = jigsawService.getJigsaw(storedJigsaw.getId());

        assertNotNull(retrievedJigsaw);
        assertEquals(storedJigsaw, retrievedJigsaw);
    }

}
