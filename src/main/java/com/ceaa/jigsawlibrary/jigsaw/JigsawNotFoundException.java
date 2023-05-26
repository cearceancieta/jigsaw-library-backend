package com.ceaa.jigsawlibrary.jigsaw;

public class JigsawNotFoundException extends RuntimeException {
    public static final String ERROR_MESSAGE_TEMPLATE = "Jigsaw with Id %s was not found";

    public JigsawNotFoundException(String id) {
        super(String.format(ERROR_MESSAGE_TEMPLATE, id));
    }
}
