package com.ceaa.jigsawlibrary.jigsaw;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface JigsawRepository {
    Mono<Jigsaw> get(String id);

    Flux<Jigsaw> find();

    Mono<Jigsaw> save(Jigsaw jigsaw);
}
