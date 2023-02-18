package com.ceaa.jigsawLibrary.repositories;

import com.ceaa.jigsawLibrary.jigsaw.Jigsaw;
import com.ceaa.jigsawLibrary.jigsaw.JigsawNotFoundException;
import com.ceaa.jigsawLibrary.jigsaw.JigsawRepository;

import java.util.Optional;
import java.util.UUID;

public class JigsawMySqlRepository implements JigsawRepository {

    private final JigsawMySqlDataSource dataSource;

    public JigsawMySqlRepository(JigsawMySqlDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Jigsaw get(UUID id) {
        Optional<JigsawEntity> foundJigsaw = dataSource.findById(id);
        if (foundJigsaw.isEmpty()) {
            throw new JigsawNotFoundException(id);
        }
        return Jigsaw.builder()
                .id(foundJigsaw.get().getId())
                .title(foundJigsaw.get().getTitle())
                .brand(foundJigsaw.get().getBrand())
                .nPieces(foundJigsaw.get().getNPieces())
                .build();
    }
}
