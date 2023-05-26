package com.ceaa.jigsawLibrary.repositories.mongodb;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("jigsaws")
public class JigsawDocument {

    @Id
    private String id;
    private String title;
    private String subtitle;
    private String collection;
    private String brand;
    private String shape;
    private Integer nPieces;

    @Override
    public String toString() {
        return String.format("JigsawDocument: {id: %s, title: %s, subtitle: %s, collection: %s, brand: %s, shape: %s, nPieces: %d}",
                id, title, subtitle, collection, brand, shape, nPieces);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof JigsawDocument)) return false;
        JigsawDocument other = (JigsawDocument) obj;
        return Objects.equals(this.getId(), other.getId())
                && this.getTitle().equals(other.getTitle())
                && Objects.equals(this.getSubtitle(), other.getSubtitle())
                && Objects.equals(this.getCollection(), other.getCollection())
                && this.getBrand().equals(other.getBrand())
                && Objects.equals(this.getShape(), other.getShape())
                && this.getNPieces().equals(other.getNPieces());
    }
}
