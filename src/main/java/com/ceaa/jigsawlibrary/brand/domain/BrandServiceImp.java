package com.ceaa.jigsawlibrary.brand.domain;

import reactor.core.publisher.Flux;

public class BrandServiceImp implements BrandService {

    private final BrandRepository repository;

    public BrandServiceImp(BrandRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<Brand> getBrands() {
        return repository.find();
    }
}
