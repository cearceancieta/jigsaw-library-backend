package com.ceaa.jigsawLibrary.jigsaw;

import java.util.UUID;

public class JigsawService {

    private final JigsawRepository repository;

    public JigsawService(JigsawRepository repository) {
        this.repository = repository;
    }

    public Jigsaw getJigsaw(UUID id) {
        return repository.get(id);
    }
}
