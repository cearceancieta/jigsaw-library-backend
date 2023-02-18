package com.ceaa.jigsawLibrary.repositories;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
public class JigsawEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    private String brand;
    private int nPieces;

    protected JigsawEntity() {}

   protected JigsawEntity(UUID id, String title, String brand, int nPieces) {
        this.id = id;
        this.title = title;
        this.brand = brand;
        this.nPieces = nPieces;
   }

    protected JigsawEntity(String title, String brand, int nPieces) {
        this.title = title;
        this.brand = brand;
        this.nPieces = nPieces;
    }
}
