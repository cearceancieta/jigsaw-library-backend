package com.ceaa.jigsawlibrary.jigsaw.controllers;

import com.ceaa.jigsawlibrary.jigsaw.domain.JigsawNotFoundException;
import com.ceaa.jigsawlibrary.shared.controllers.Error;
import com.ceaa.jigsawlibrary.shared.controllers.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JigsawNotFoundAdviceTest {

    @Test
    void handleJigsawNotFoundException() {
        JigsawNotFoundException exception = new JigsawNotFoundException("id");

        JigsawNotFoundAdvice advice = new JigsawNotFoundAdvice();
        Error error = advice.jigsawNotFoundHandler(exception).block();

        assertThat(error.code()).isEqualTo(ErrorCode.RESOURCE_NOT_FOUND);
        assertThat(error.message()).isEqualTo(exception.getMessage());
    }

}