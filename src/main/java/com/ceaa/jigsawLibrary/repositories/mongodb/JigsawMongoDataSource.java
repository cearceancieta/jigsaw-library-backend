package com.ceaa.jigsawLibrary.repositories.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface JigsawMongoDataSource extends MongoRepository<JigsawDocument, String> {

}
