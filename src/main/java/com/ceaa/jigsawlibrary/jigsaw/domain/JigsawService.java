package com.ceaa.jigsawlibrary.jigsaw.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface JigsawService {

    Mono<Jigsaw> getJigsaw(String id);

    Flux<Jigsaw> getJigsaws();

    Mono<Jigsaw> saveJigsaw(Jigsaw jigsaw);
}
