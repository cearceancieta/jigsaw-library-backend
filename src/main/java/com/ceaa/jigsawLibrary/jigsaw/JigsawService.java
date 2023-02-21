package com.ceaa.jigsawLibrary.jigsaw;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JigsawService {

    private final JigsawRepository repository;

    public JigsawService(JigsawRepository repository) {
        this.repository = repository;
    }

    public Jigsaw getJigsaw(UUID id) {
        return repository.get(id);
    }
}
