package com.ceaa.jigsawlibrary.jigsaw;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class JigsawService {

    private final JigsawRepository repository;

    public JigsawService(JigsawRepository repository) {
        this.repository = repository;
    }

    public Mono<Jigsaw> getJigsaw(String id) {
        return repository.get(id)
                .switchIfEmpty(Mono.error(new JigsawNotFoundException(id)));
    }

    public Flux<Jigsaw> getJigsaws() {
        return repository.find();
    }

    public Mono<Jigsaw> saveJigsaw(Jigsaw jigsaw) {
        return repository.save(jigsaw);
    }
}
