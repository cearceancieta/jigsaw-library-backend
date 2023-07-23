package com.ceaa.jigsawlibrary.jigsaw.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Jigsaw {
    private String id;
    private String title;
    private String subtitle;
    private String collection;
    private String brand;
    private String shape;
    private Integer nPieces;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() { return subtitle; }

    public String getCollection() { return collection; }

    public String getBrand() {
        return brand;
    }

    public String getShape() { return shape; }

    public Integer getNPieces() {
        return nPieces;
    }

    @Override
    public String toString() {
        return String.format("Jigsaw: {id: %s, title: %s, subtitle: %s, collection: %s, brand: %s, shape: %s, nPieces: %d}",
                id, title, subtitle, collection, brand, shape, nPieces);
    }
}
