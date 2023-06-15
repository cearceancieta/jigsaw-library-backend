package com.ceaa.jigsawlibrary.integrationtests;

import com.ceaa.jigsawlibrary.controllers.Error;
import com.ceaa.jigsawlibrary.controllers.ErrorCode;
import com.ceaa.jigsawlibrary.controllers.ValidationExceptionsAdvice;
import com.ceaa.jigsawlibrary.jigsaw.Jigsaw;
import com.ceaa.jigsawlibrary.jigsaw.JigsawNotFoundException;
import com.ceaa.jigsawlibrary.repositories.mongodb.JigsawDocument;
import com.ceaa.jigsawlibrary.repositories.mongodb.JigsawDocumentMapper;
import com.ceaa.jigsawlibrary.repositories.mongodb.JigsawMongoDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JigsawIT {

    @Autowired
    private JigsawMongoDataSource dataSource;

    @LocalServerPort
    private int port;

    WebTestClient client;
    private JigsawDocumentMapper mapper;

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port + "/jigsaws").build();
        mapper = new JigsawDocumentMapper();
    }

    @AfterEach
    void tearDown() {
        dataSource.deleteAll().block();
    }

    @Test
    void whenGettingJigsawThatDoesntExistShouldReturnHttpStatusNotFound() {
        String id = "JigsawID";
        client.get().uri("/" + id).exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Error.class)
                .consumeWith(result -> {
                    assertThat(result.getResponseBody()).isNotNull();
                    assertThat(result.getResponseBody().code())
                            .isEqualTo(ErrorCode.RESOURCE_NOT_FOUND);
                    assertThat(result.getResponseBody().message())
                            .isEqualTo(String.format(JigsawNotFoundException.ERROR_MESSAGE_TEMPLATE, id));
                });
    }

    @Test
    void getJigsawShouldReturnStoredJigsaw() {
        JigsawDocument storedJigsaw = dataSource.save(JigsawDocument.builder()
                        .id("id").title("title").brand("brand").shape("shape").nPieces(1000)
                        .build()).block();

        client.get().uri("/" + storedJigsaw.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Jigsaw.class)
                .consumeWith(result -> {
                    assertThat(result.getResponseBody()).isNotNull();
                    assertThat(result.getResponseBody()).isEqualTo(mapper.mapFromDocument(storedJigsaw));
                });
    }

    @Test
    void whenGettingJigsawsAndNoneAreStoredListShouldReturnEmptyList() {
        client.get().exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Jigsaw.class)
                .hasSize(0);
    }

    @Test
    void getAllJigsaws() {
        List<Jigsaw> storedJigsaws = Flux.range(1,3)
                .map(i -> JigsawDocument.builder()
                        .title("title" + i).brand("brand" + i).shape("shape" + i).nPieces(500 * i)
                        .build())
                .flatMap(dataSource::save)
                .map(mapper::mapFromDocument)
                .collectList()
                .block();

        client.get().exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Jigsaw.class)
                .consumeWith(result -> {
                    assertThat(result.getResponseBody()).isNotNull();
                    assertThat(result.getResponseBody()).hasSameElementsAs(storedJigsaws);
                    assertThat(result.getResponseBody()).containsAll(storedJigsaws);
                });
    }

    @Test
    void createNewJigsaw() {
        Jigsaw jigsawToCreate = Jigsaw.builder()
                .title("title").brand("brand").subtitle("subtitle").collection("collection")
                .shape("shape").nPieces(500)
                .build();

        EntityExchangeResult<Jigsaw> result = client.post()
                .bodyValue(jigsawToCreate)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isCreated()
                .expectBody(Jigsaw.class)
                .returnResult();

        assertThat(result.getResponseBody()).isNotNull();
        Jigsaw returnedJigsaw = result.getResponseBody();
        assertThat(returnedJigsaw.getId()).isNotBlank();
        assertThat(result.getResponseHeaders().getLocation()).isNotNull();
        assertThat(result.getResponseHeaders().getLocation().toString())
                .endsWith("/jigsaws/" + returnedJigsaw.getId());

        JigsawDocument storedJigsaw = dataSource.findById(returnedJigsaw.getId()).block();

        assertThat(storedJigsaw).isNotNull();
        assertThat(storedJigsaw.getTitle()).isEqualTo(jigsawToCreate.getTitle());
        assertThat(storedJigsaw.getSubtitle()).isEqualTo(jigsawToCreate.getSubtitle());
        assertThat(storedJigsaw.getCollection()).isEqualTo(jigsawToCreate.getCollection());
        assertThat(storedJigsaw.getBrand()).isEqualTo(jigsawToCreate.getBrand());
        assertThat(storedJigsaw.getShape()).isEqualTo(jigsawToCreate.getShape());
        assertThat(storedJigsaw.getNPieces()).isEqualTo(jigsawToCreate.getNPieces());

        assertThat(returnedJigsaw.getTitle()).isEqualTo(jigsawToCreate.getTitle());
        assertThat(returnedJigsaw.getSubtitle()).isEqualTo(jigsawToCreate.getSubtitle());
        assertThat(returnedJigsaw.getCollection()).isEqualTo(jigsawToCreate.getCollection());
        assertThat(returnedJigsaw.getBrand()).isEqualTo(jigsawToCreate.getBrand());
        assertThat(returnedJigsaw.getShape()).isEqualTo(jigsawToCreate.getShape());
        assertThat(returnedJigsaw.getNPieces()).isEqualTo(jigsawToCreate.getNPieces());
    }

    @ParameterizedTest
    @MethodSource
    void whenCreatingNewJigsawWithoutAllRequiredFields(ImmutablePair<Jigsaw, Map<String, String>> testData) {
        client.post().bodyValue(testData.getLeft()).exchange()
                .expectStatus().isBadRequest()
                .expectBody(Error.class)
                .consumeWith(result -> {
                    assertThat(result.getResponseBody()).isNotNull();
                    assertThat(result.getResponseBody().code()).isEqualTo(ErrorCode.BAD_REQUEST);
                    assertThat(result.getResponseBody().message()).isEqualTo(ValidationExceptionsAdvice.ERROR_MESSAGE);
                    Map<String, String> errorMap = result.getResponseBody().details();
                    Map<String, String> expectedErrorMap = testData.getRight();
                    assertThat(errorMap).hasSameSizeAs(expectedErrorMap);
                    expectedErrorMap.forEach((field, message) -> {
                        assertThat(errorMap).containsKey(field);
                        assertThat(errorMap).containsEntry(field, message);
                    });
                });
    }

    private static Stream<ImmutablePair<Jigsaw, Map<String, String>>> whenCreatingNewJigsawWithoutAllRequiredFields() {
        final String blankFieldMessage = "must not be blank";
        final String nullFieldMessage = "must not be null";
        final String notPositiveFieldMessage = "must be greater than 0";
        Stream<ImmutablePair<Jigsaw, Map<String, String>>> jigsawStream = Stream.of(
                new ImmutablePair<>(Jigsaw.builder().build(),
                        Map.of("title", blankFieldMessage, "brand", blankFieldMessage,
                                "shape", blankFieldMessage, "nPieces", nullFieldMessage)),
                new ImmutablePair<>(Jigsaw.builder().brand("brand").shape("shape").nPieces(500).build(),
                        Map.of("title", blankFieldMessage)),
                new ImmutablePair<>(Jigsaw.builder().title("title").shape("shape").nPieces(500).build(),
                        Map.of("brand", blankFieldMessage)),
                new ImmutablePair<>(Jigsaw.builder().title("title").brand("brand").nPieces(500).build(),
                        Map.of("shape", blankFieldMessage)),
                new ImmutablePair<>(Jigsaw.builder().title("title").brand("brand").shape("shape").build(),
                        Map.of("nPieces", nullFieldMessage)),
                new ImmutablePair<>(Jigsaw.builder().title("title").brand("brand").shape("shape").nPieces(0).build(),
                        Map.of("nPieces", notPositiveFieldMessage)),
                new ImmutablePair<>(Jigsaw.builder().title("title").brand("brand").shape("shape").nPieces(-1).build(),
                        Map.of("nPieces", notPositiveFieldMessage))
        );
        return jigsawStream;
    }
}
