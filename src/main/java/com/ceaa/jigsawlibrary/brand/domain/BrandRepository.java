package com.ceaa.jigsawlibrary.brand.domain;

import reactor.core.publisher.Flux;

public interface BrandRepository {
    Flux<Brand> find();
}
