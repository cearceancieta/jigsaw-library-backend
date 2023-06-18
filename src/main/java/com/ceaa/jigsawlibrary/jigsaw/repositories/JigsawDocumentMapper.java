package com.ceaa.jigsawlibrary.jigsaw.repositories;

import com.ceaa.jigsawlibrary.jigsaw.domain.Jigsaw;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JigsawDocumentMapper {
    public Jigsaw mapFromDocument(JigsawDocument document) {
        return Jigsaw.builder()
                .id(document.getId())
                .title(document.getTitle())
                .subtitle(document.getSubtitle())
                .collection(document.getCollection())
                .brand(document.getBrand())
                .shape(document.getShape())
                .nPieces(document.getNPieces())
                .build();
    }

    public JigsawDocument mapToDocument(Jigsaw jigsaw) {
        return JigsawDocument.builder()
                .id(jigsaw.getId())
                .title(jigsaw.getTitle())
                .subtitle(jigsaw.getSubtitle())
                .collection(jigsaw.getCollection())
                .brand(jigsaw.getBrand())
                .shape(jigsaw.getShape())
                .nPieces(jigsaw.getNPieces())
                .build();
    }

    public List<JigsawDocument> mapToDocumentList(List<Jigsaw> jigsaws) {
        return jigsaws.stream().map(this::mapToDocument).toList();
    }

    public List<Jigsaw> mapFromDocumentList(List<JigsawDocument> documents) {
        return documents.stream().map(this::mapFromDocument).toList();
    }
}
