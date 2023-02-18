package com.ceaa.jigsawLibrary.jigsaw;

import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Builder
@EqualsAndHashCode
public class Jigsaw {
    private UUID id;
    private String title;
    private String brand;
    private int nPieces;

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBrand() {
        return brand;
    }

    public int getNPieces() {
        return nPieces;
    }
}
