package com.ceaa.jigsawlibrary.jigsaw;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    @NotBlank(message = "must not be blank")
    private String title;
    private String subtitle;
    private String collection;
    @NotBlank(message = "must not be blank")
    private String brand;
    @NotBlank(message = "must not be blank")
    private String shape;
    @NotNull(message = "must not be null")
    @Positive(message = "must be greater than 0")
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

    public void setNPieces(Integer nPieces) { this.nPieces = nPieces; }

    @Override
    public String toString() {
        return String.format("Jigsaw: {id: %s, title: %s, subtitle: %s, collection: %s, brand: %s, shape: %s, nPieces: %d}",
                id, title, subtitle, collection, brand, shape, nPieces);
    }
}
