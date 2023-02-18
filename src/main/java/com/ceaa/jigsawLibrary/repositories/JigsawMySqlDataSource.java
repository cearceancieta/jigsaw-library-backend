package com.ceaa.jigsawLibrary.repositories;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface JigsawMySqlDataSource extends CrudRepository<JigsawEntity, UUID> {

}
