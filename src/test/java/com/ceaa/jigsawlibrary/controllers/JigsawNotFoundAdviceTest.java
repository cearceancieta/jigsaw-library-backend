package com.ceaa.jigsawlibrary.controllers;

import com.ceaa.jigsawlibrary.jigsaw.JigsawNotFoundException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JigsawNotFoundAdviceTest {

    @Test
    void handleJigsawNotFoundException() {
        JigsawNotFoundException exception = new JigsawNotFoundException("id");

        JigsawNotFoundAdvice advice = new JigsawNotFoundAdvice();
        String message = advice.jigsawNotFoundHandler(exception);

        assertThat(message).isEqualTo(exception.getMessage());
    }

}