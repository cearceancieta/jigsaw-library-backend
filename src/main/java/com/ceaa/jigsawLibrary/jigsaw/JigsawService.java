package com.ceaa.jigsawLibrary.jigsaw;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JigsawService {

    private final JigsawRepository repository;

    public JigsawService(JigsawRepository repository) {
        this.repository = repository;
    }

    public Jigsaw getJigsaw(String id) {
        return repository.get(id);
    }

    public List<Jigsaw> getJigsaws() {
        return repository.find();
    }

    public Jigsaw saveJigsaw(Jigsaw jigsaw) {
        return repository.save(jigsaw);
    }
}
