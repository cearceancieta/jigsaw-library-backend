package com.ceaa.jigsawlibrary.config;

import com.ceaa.jigsawlibrary.jigsaw.domain.JigsawRepository;
import com.ceaa.jigsawlibrary.jigsaw.domain.JigsawService;
import com.ceaa.jigsawlibrary.jigsaw.domain.JigsawServiceImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicesConfig {

    @Bean
    public JigsawService jigsawService(JigsawRepository repository) {
        return new JigsawServiceImp(repository);
    }

}
