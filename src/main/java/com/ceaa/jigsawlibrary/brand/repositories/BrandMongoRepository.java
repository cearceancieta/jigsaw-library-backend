package com.ceaa.jigsawlibrary.brand.repositories;

import com.ceaa.jigsawlibrary.brand.domain.Brand;
import com.ceaa.jigsawlibrary.brand.domain.BrandRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class BrandMongoRepository implements BrandRepository {
    private final BrandMongoDataSource dataSource;
    private final ModelMapper mapper;

    public BrandMongoRepository(BrandMongoDataSource dataSource, ModelMapper mapper) {
        this.dataSource = dataSource;
        this.mapper = mapper;
    }

    @Override
    public Flux<Brand> find() {
        log.info("Searching for all Brands in database");
        return dataSource.findAll()
                .map(document -> mapper.map(document, Brand.class));
    }
}
