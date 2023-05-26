package com.ceaa.jigsawLibrary.jigsaw;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class JigsawTest {

    private static final Jigsaw jigsaw = Jigsaw.builder()
            .id("id").title("title").subtitle("subtitle").collection("collection")
            .brand("brand").shape("shape").nPieces(1000)
            .build();

    @Test
    void testEquals_exactSameObject() {
        assertTrue(jigsaw.equals(jigsaw));
    }

    @Test
    void testEquals_objectOfDifferentClass() {
        String otherObject = new String();
        assertFalse(jigsaw.equals(otherObject));
    }

    @ParameterizedTest
    @MethodSource
    void testEquals_jigsawsWithDifferentFields(Jigsaw other) {
        assertFalse(jigsaw.equals(other));
    }

    @Test
    void testEquals_jigsawWithSameFields() {
        Jigsaw other = Jigsaw.builder()
                .id(jigsaw.getId()).title(jigsaw.getTitle()).subtitle(jigsaw.getSubtitle())
                .collection(jigsaw.getCollection()).brand(jigsaw.getBrand()).shape(jigsaw.getShape())
                .nPieces(jigsaw.getNPieces())
                .build();
        assertTrue(jigsaw.equals(other));
    }

    @Test
    void testEquals_sameJigsawsWithOnlyRequiredFields() {
        Jigsaw basicJigsaw = Jigsaw.builder()
                .title("title").brand("brand").shape("shape").nPieces(500).build();
        Jigsaw otherBasicJigsaw = Jigsaw.builder()
                .title(basicJigsaw.getTitle()).brand(basicJigsaw.getBrand()).shape(basicJigsaw.getShape())
                .nPieces(basicJigsaw.getNPieces()).build();
        assertTrue(basicJigsaw.equals(otherBasicJigsaw));
    }

    private static Stream<Jigsaw> testEquals_jigsawsWithDifferentFields() {
        return Stream.of(
                Jigsaw.builder()
                        .id("otherID").title(jigsaw.getTitle()).subtitle(jigsaw.getSubtitle())
                        .collection(jigsaw.getCollection()).brand(jigsaw.getBrand()).shape(jigsaw.getShape())
                        .nPieces(jigsaw.getNPieces())
                        .build(),
                Jigsaw.builder()
                        .id(jigsaw.getId()).title("otherTitle").subtitle(jigsaw.getSubtitle())
                        .collection(jigsaw.getCollection()).brand(jigsaw.getBrand()).shape(jigsaw.getShape())
                        .nPieces(jigsaw.getNPieces())
                        .build(),
                Jigsaw.builder()
                        .id(jigsaw.getId()).title(jigsaw.getTitle()).subtitle("otherSubtitle")
                        .collection(jigsaw.getCollection()).brand(jigsaw.getBrand()).shape(jigsaw.getShape())
                        .nPieces(jigsaw.getNPieces())
                        .build(),
                Jigsaw.builder()
                        .id(jigsaw.getId()).title(jigsaw.getTitle()).subtitle(jigsaw.getSubtitle())
                        .collection("otherCollection").brand(jigsaw.getBrand()).shape(jigsaw.getShape())
                        .nPieces(jigsaw.getNPieces())
                        .build(),
                Jigsaw.builder()
                        .id(jigsaw.getId()).title(jigsaw.getTitle()).subtitle(jigsaw.getSubtitle())
                        .collection(jigsaw.getCollection()).brand("otherBrand").shape(jigsaw.getShape())
                        .nPieces(jigsaw.getNPieces())
                        .build(),
                Jigsaw.builder()
                        .id(jigsaw.getId()).title(jigsaw.getTitle()).subtitle(jigsaw.getSubtitle())
                        .collection(jigsaw.getCollection()).brand(jigsaw.getBrand()).shape("otherShape")
                        .nPieces(jigsaw.getNPieces())
                        .build(),
                Jigsaw.builder()
                        .id(jigsaw.getId()).title(jigsaw.getTitle()).subtitle(jigsaw.getSubtitle())
                        .collection(jigsaw.getCollection()).brand(jigsaw.getBrand()).shape(jigsaw.getShape())
                        .nPieces(-1)
                        .build()
        );
    }
}