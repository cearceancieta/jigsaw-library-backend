package com.ceaa.jigsawLibrary.repositories;

import com.ceaa.jigsawLibrary.jigsaw.Jigsaw;
import com.ceaa.jigsawLibrary.jigsaw.JigsawNotFoundException;
import com.ceaa.jigsawLibrary.jigsaw.JigsawRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class JigsawMySqlRepository implements JigsawRepository {

    private final JigsawMySqlDataSource dataSource;

    public JigsawMySqlRepository(JigsawMySqlDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Jigsaw get(UUID id) {
        log.info("Searching for Jigsaw with id {} in database", id);
        JigsawEntity foundJigsaw = dataSource.findById(id)
                .orElseThrow(() -> new JigsawNotFoundException(id));
        return Jigsaw.builder()
                .id(foundJigsaw.getId())
                .title(foundJigsaw.getTitle())
                .brand(foundJigsaw.getBrand())
                .nPieces(foundJigsaw.getNPieces())
                .build();
    }
}
