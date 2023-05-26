package com.ceaa.jigsawlibrary.repositories.mongodb;

import com.ceaa.jigsawlibrary.jigsaw.Jigsaw;
import com.ceaa.jigsawlibrary.jigsaw.JigsawNotFoundException;
import com.ceaa.jigsawlibrary.jigsaw.JigsawRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public Jigsaw get(String id) {
        log.info("Searching for Jigsaw with id [{}] in database", id);
        JigsawDocument foundJigsaw = dataSource.findById(id)
                .orElseThrow(() -> new JigsawNotFoundException(id));
        return mapper.mapFromDocument(foundJigsaw);
    }

    @Override
    public List<Jigsaw> find() {
        log.info("Searching for all Jigsaws in database");
        return dataSource.findAll().stream()
                .map(mapper::mapFromDocument)
                .collect(Collectors.toList());
    }

    @Override
    public Jigsaw save(Jigsaw jigsaw) {
        log.info("Saving Jigsaw [{}] in database", jigsaw);
        JigsawDocument document = dataSource.save(mapper.mapToDocument(jigsaw));
        return mapper.mapFromDocument(document);
    }

}
