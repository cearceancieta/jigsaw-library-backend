package com.ceaa.jigsawlibrary.brand.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandMongoDataSource extends ReactiveCrudRepository<BrandDocument, String> {
}
