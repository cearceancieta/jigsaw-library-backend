package com.ceaa.jigsawLibrary.config;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "com.ceaa.jigsawLibrary.repositories.mongodb")
public class PersistenceConfig {
}
