package com.ceaa.jigsawLibrary.jigsaw;

import java.util.UUID;

public class JigsawNotFoundException extends RuntimeException {
    public JigsawNotFoundException(UUID id) {
        super(String.format("Jigsaw with Id %s was not found", id));
    }
}
