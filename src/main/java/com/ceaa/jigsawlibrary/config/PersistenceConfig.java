package com.ceaa.jigsawlibrary.config;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "com.ceaa.jigsawlibrary.repositories.mongodb")
public class PersistenceConfig {
}
