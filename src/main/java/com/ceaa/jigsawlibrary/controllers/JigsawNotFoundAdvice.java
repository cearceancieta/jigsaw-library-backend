package com.ceaa.jigsawlibrary.controllers;

import com.ceaa.jigsawlibrary.jigsaw.JigsawNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class JigsawNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(JigsawNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String jigsawNotFoundHandler(JigsawNotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return exception.getMessage();
    }
}
