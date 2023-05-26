package com.ceaa.jigsawlibrary.jigsaw;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class JigsawNotFoundExceptionTest {

    @Test
    void errorMessageTemplateIsCorrect(){
        assertThat(JigsawNotFoundException.ERROR_MESSAGE_TEMPLATE)
                .isEqualTo("Jigsaw with Id %s was not found");
    }

    @Test
    void errorMessageIsSetCorrectly() {
        String id = "jigsawID";
        JigsawNotFoundException exception = new JigsawNotFoundException(id);
        assertThat(exception.getMessage())
                .isEqualTo("Jigsaw with Id " + id + " was not found");
    }

}