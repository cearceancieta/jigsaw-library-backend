package com.ceaa.jigsawLibrary.repositories.mongodb;

import com.ceaa.jigsawLibrary.jigsaw.Jigsaw;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JigsawDocumentMapperTest {

    private JigsawDocumentMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new JigsawDocumentMapper();
    }

    @Test
    void mapFromDocument_correctMapping() {
        JigsawDocument document = JigsawDocument.builder().id("id").title("title").subtitle("subtitle")
                .collection("collection").brand("brand").shape("shape").nPieces(500).build();
        Jigsaw jigsaw = mapper.mapFromDocument(document);

        assertThat(jigsaw.getId()).isEqualTo(document.getId());
        assertThat(jigsaw.getTitle()).isEqualTo(document.getTitle());
        assertThat(jigsaw.getBrand()).isEqualTo(document.getBrand());
        assertThat(jigsaw.getNPieces()).isEqualTo(document.getNPieces());
    }

    @Test
    void mapToDocument_correctMapping() {
        Jigsaw jigsaw = Jigsaw.builder().id("id").title("title").subtitle("subtitle")
                .collection("collection").brand("brand").shape("shape").nPieces(500).build();
        JigsawDocument document = mapper.mapToDocument(jigsaw);

        assertThat(document.getId()).isEqualTo(jigsaw.getId());
        assertThat(document.getTitle()).isEqualTo(jigsaw.getTitle());
        assertThat(document.getBrand()).isEqualTo(jigsaw.getBrand());
        assertThat(document.getNPieces()).isEqualTo(jigsaw.getNPieces());
    }

    @Test
    void mapToDocumentList_correctMapping() {
        List<Jigsaw> jigsaws = Arrays.asList(
                Jigsaw.builder().id("id1").title("title1").subtitle("subtitle1")
                        .collection("collection1").brand("brand1").shape("shape1").nPieces(500).build(),
                Jigsaw.builder().id("id2").title("title2").subtitle("subtitle2")
                        .collection("collection2").brand("brand2").shape("shape2").nPieces(1000).build());
        List<JigsawDocument> expectedList = jigsaws.stream()
                .map(mapper::mapToDocument)
                .toList();

        List<JigsawDocument> documents = mapper.mapToDocumentList(jigsaws);

        assertThat(documents).containsAll(expectedList);
    }

    @Test
    void mapToJigsawList_correctMapping() {
        List<JigsawDocument> documents = Arrays.asList(
                JigsawDocument.builder().id("id1").title("title1").subtitle("subtitle1")
                        .collection("collection1").brand("brand1").shape("shape1").nPieces(500).build(),
                JigsawDocument.builder().id("id2").title("title2").subtitle("subtitle2")
                        .collection("collection2").brand("brand2").shape("shape2").nPieces(1000).build());
        List<Jigsaw> expectedList = documents.stream()
                .map(mapper::mapFromDocument)
                .toList();

        List<Jigsaw> jigsaws = mapper.mapFromDocumentList(documents);

        assertThat(jigsaws).containsAll(expectedList);
    }
}