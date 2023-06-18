package com.ceaa.jigsawlibrary.jigsaw.repositories;

import com.ceaa.jigsawlibrary.jigsaw.domain.Jigsaw;
import com.ceaa.jigsawlibrary.jigsaw.domain.JigsawRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class JigsawMongoRepository implements JigsawRepository {

    private final JigsawMongoDataSource dataSource;
    private final JigsawDocumentMapper mapper;

    public JigsawMongoRepository(JigsawMongoDataSource dataSource,
                                 JigsawDocumentMapper mapper) {
        this.dataSource = dataSource;
        this.mapper = mapper;
    }

    @Override
    public Mono<Jigsaw> get(String id) {
        log.info("Searching for Jigsaw with id [{}] in database", id);
        return dataSource.findById(id)
                .map(mapper::mapFromDocument);
    }

    @Override
    public Flux<Jigsaw> find() {
        log.info("Searching for all Jigsaws in database");
        return dataSource.findAll()
                .map(mapper::mapFromDocument);
    }

    @Override
    public Mono<Jigsaw> save(Jigsaw jigsaw) {
        log.info("Saving Jigsaw [{}] in database", jigsaw);
        return dataSource.save(mapper.mapToDocument(jigsaw))
                .map(mapper::mapFromDocument);
    }

}
