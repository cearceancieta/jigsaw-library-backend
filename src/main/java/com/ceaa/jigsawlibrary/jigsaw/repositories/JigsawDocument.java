package com.ceaa.jigsawlibrary.jigsaw.repositories;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
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
}
