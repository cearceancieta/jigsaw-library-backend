package com.ceaa.jigsawlibrary.jigsaw.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JigsawMongoDataSource extends ReactiveCrudRepository<JigsawDocument, String> {

}