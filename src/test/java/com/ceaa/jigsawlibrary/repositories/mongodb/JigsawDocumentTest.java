package com.ceaa.jigsawlibrary.repositories.mongodb;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class JigsawDocumentTest {

    private static final JigsawDocument jigsaw = JigsawDocument.builder()
            .id("id").title("title").subtitle("subtitle").collection("collection")
            .brand("brand").shape("shape").nPieces(1000)
            .build();

    @Test
    void testEquals_exactSameObject() {
        assertEquals(jigsaw, jigsaw);
    }

    @Test
    void testEquals_objectOfDifferentClass() {
        String otherObject = new String();
        assertNotEquals(jigsaw, otherObject);
    }

    @ParameterizedTest
    @MethodSource
    void testEquals_jigsawsWithDifferentFields(JigsawDocument other) {
        assertNotEquals(jigsaw, other);
    }

    @Test
    void testEquals_jigsawWithSameFields() {
        JigsawDocument other = JigsawDocument.builder()
                .id(jigsaw.getId()).title(jigsaw.getTitle()).subtitle(jigsaw.getSubtitle())
                .collection(jigsaw.getCollection()).brand(jigsaw.getBrand()).shape(jigsaw.getShape())
                .nPieces(jigsaw.getNPieces())
                .build();
        assertEquals(jigsaw, other);
    }

    @Test
    void testEquals_sameJigsawsWithOnlyRequiredFields() {
        JigsawDocument basicJigsaw = JigsawDocument.builder()
                .title("title").brand("brand").shape("shape").nPieces(500).build();
        JigsawDocument otherBasicJigsaw = JigsawDocument.builder()
                .title(basicJigsaw.getTitle()).brand(basicJigsaw.getBrand()).shape(basicJigsaw.getShape())
                .nPieces(basicJigsaw.getNPieces()).build();
        assertEquals(basicJigsaw, otherBasicJigsaw);
    }

    private static Stream<JigsawDocument> testEquals_jigsawsWithDifferentFields() {
        return Stream.of(
                JigsawDocument.builder()
                        .id("otherID").title(jigsaw.getTitle()).subtitle(jigsaw.getSubtitle())
                        .collection(jigsaw.getCollection()).brand(jigsaw.getBrand()).shape(jigsaw.getShape())
                        .nPieces(jigsaw.getNPieces())
                        .build(),
                JigsawDocument.builder()
                        .id(jigsaw.getId()).title("otherTitle").subtitle(jigsaw.getSubtitle())
                        .collection(jigsaw.getCollection()).brand(jigsaw.getBrand()).shape(jigsaw.getShape())
                        .nPieces(jigsaw.getNPieces())
                        .build(),
                JigsawDocument.builder()
                        .id(jigsaw.getId()).title(jigsaw.getTitle()).subtitle("otherSubtitle")
                        .collection(jigsaw.getCollection()).brand(jigsaw.getBrand()).shape(jigsaw.getShape())
                        .nPieces(jigsaw.getNPieces())
                        .build(),
                JigsawDocument.builder()
                        .id(jigsaw.getId()).title(jigsaw.getTitle()).subtitle(jigsaw.getSubtitle())
                        .collection("otherCollection").brand(jigsaw.getBrand()).shape(jigsaw.getShape())
                        .nPieces(jigsaw.getNPieces())
                        .build(),
                JigsawDocument.builder()
                        .id(jigsaw.getId()).title(jigsaw.getTitle()).subtitle(jigsaw.getSubtitle())
                        .collection(jigsaw.getCollection()).brand("otherBrand").shape(jigsaw.getShape())
                        .nPieces(jigsaw.getNPieces())
                        .build(),
                JigsawDocument.builder()
                        .id(jigsaw.getId()).title(jigsaw.getTitle()).subtitle(jigsaw.getSubtitle())
                        .collection(jigsaw.getCollection()).brand(jigsaw.getBrand()).shape("otherShape")
                        .nPieces(jigsaw.getNPieces())
                        .build(),
                JigsawDocument.builder()
                        .id(jigsaw.getId()).title(jigsaw.getTitle()).subtitle(jigsaw.getSubtitle())
                        .collection(jigsaw.getCollection()).brand(jigsaw.getBrand()).shape(jigsaw.getShape())
                        .nPieces(-1)
                        .build()
        );
    }

}