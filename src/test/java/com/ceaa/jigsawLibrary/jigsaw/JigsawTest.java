package com.ceaa.jigsawLibrary.jigsaw;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class JigsawTest {

    @Mock JigsawRepository repository;

    @Test
    void jigsawDoesNotExist() {
        UUID lookedForId = UUID.randomUUID();

        JigsawService jigsawService = new JigsawService(repository);
        lenient().when(repository.get(lookedForId)).thenThrow(new JigsawNotFoundException(lookedForId));

        Exception exception = assertThrows(JigsawNotFoundException.class,
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

        JigsawService jigsawService = new JigsawService(repository);
        lenient().when(repository.get(storedJigsaw.getId())).thenReturn(storedJigsaw);

        Jigsaw retrievedJigsaw = jigsawService.getJigsaw(storedJigsaw.getId());

        assertNotNull(retrievedJigsaw);
        assertEquals(storedJigsaw, retrievedJigsaw);
    }

}
