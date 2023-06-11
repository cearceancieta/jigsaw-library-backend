package com.ceaa.jigsawlibrary.jigsaw;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    public void setNPieces(Integer nPieces) { this.nPieces = nPieces; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Jigsaw other)) return false;
        return Objects.equals(this.getId(), other.getId())
                && this.getTitle().equals(other.getTitle())
                && Objects.equals(this.getSubtitle(), other.getSubtitle())
                && Objects.equals(this.getCollection(), other.getCollection())
                && this.getBrand().equals(other.getBrand())
                && Objects.equals(this.getShape(), other.getShape())
                && this.getNPieces().equals(other.getNPieces());
    }

    @Override
    public String toString() {
        return String.format("Jigsaw: {id: %s, title: %s, subtitle: %s, collection: %s, brand: %s, shape: %s, nPieces: %d}",
                id, title, subtitle, collection, brand, shape, nPieces);
    }
}
