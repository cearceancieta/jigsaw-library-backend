package com.ceaa.jigsawLibrary.integrationtests;

import com.ceaa.jigsawLibrary.repositories.JigsawEntity;
import com.ceaa.jigsawLibrary.repositories.JigsawMySqlDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.UUID;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JigsawIT {

    @Autowired
    private JigsawMySqlDataSource dataSource;

    @LocalServerPort
    private int port;

    WebTestClient client;

    @BeforeEach
    void init() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
    }

    @Test
    void whenGettingJigsawThatDoesntExistShouldReturnHttpStatusNotFound() {
        client.get().uri("/jigsaws/"+ UUID.randomUUID()).exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getJigsawShouldReturnStoredJigsaw() {
        JigsawEntity storedJigsaw = dataSource.save(new JigsawEntity("title", "brand", 1000));

        client.get().uri("/jigsaws/" + storedJigsaw.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("id").isEqualTo(storedJigsaw.getId().toString())
                .jsonPath("title").isEqualTo(storedJigsaw.getTitle())
                .jsonPath("brand").isEqualTo(storedJigsaw.getBrand())
                .jsonPath("npieces").isEqualTo(1000);
    }

}
