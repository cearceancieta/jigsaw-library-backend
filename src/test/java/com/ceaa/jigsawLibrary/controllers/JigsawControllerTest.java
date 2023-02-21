package com.ceaa.jigsawLibrary.controllers;

import com.ceaa.jigsawLibrary.jigsaw.Jigsaw;
import com.ceaa.jigsawLibrary.jigsaw.JigsawNotFoundException;
import com.ceaa.jigsawLibrary.jigsaw.JigsawService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JigsawControllerTest {

    @Mock
    private JigsawService service;
    private JigsawController controller;

    @BeforeEach
    void setUp() {
        controller = new JigsawController(service);
    }

    @Test
    void getOne_jigsawNotFound() {
        UUID id = UUID.randomUUID();
        JigsawNotFoundException exceptionThrownByService = new JigsawNotFoundException(id);
        when(service.getJigsaw(id)).thenThrow(exceptionThrownByService);

        Exception exceptionThrownByController = assertThrows(JigsawNotFoundException.class,
                () -> controller.getOne(id));

        assertEquals(exceptionThrownByService, exceptionThrownByController);
    }

    @Test
    void getOne_jigsawFound() {
        Jigsaw storedJigsaw = Jigsaw.builder()
                .id(UUID.randomUUID())
                .title("Jigsaw title")
                .brand("Jigsaw brand")
                .nPieces(500)
                .build();
        when(service.getJigsaw(storedJigsaw.getId())).thenReturn(storedJigsaw);

        EntityModel<Jigsaw> jigsawModel = controller.getOne(storedJigsaw.getId());

        assertEquals(storedJigsaw, jigsawModel.getContent());
        assertTrue(jigsawModel.getLinks().hasLink("self"));
        assertEquals(String.format("/jigsaws/%s",storedJigsaw.getId()), jigsawModel.getLink("self").get().getHref());
    }

}
