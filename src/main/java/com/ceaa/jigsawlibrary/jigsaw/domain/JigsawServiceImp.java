package com.ceaa.jigsawlibrary.jigsaw.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class JigsawServiceImp implements JigsawService {

    private final JigsawRepository repository;

    public JigsawServiceImp(JigsawRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Jigsaw> getJigsaw(String id) {
        return repository.get(id)
                .switchIfEmpty(Mono.error(new JigsawNotFoundException(id)));
    }

    @Override
    public Flux<Jigsaw> getJigsaws() {
        return repository.find();
    }

    @Override
    public Mono<Jigsaw> saveJigsaw(Jigsaw jigsaw) {
        return repository.save(jigsaw);
    }
}
