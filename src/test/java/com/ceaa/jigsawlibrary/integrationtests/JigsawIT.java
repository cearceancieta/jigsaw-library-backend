package com.ceaa.jigsawlibrary.integrationtests;

import com.ceaa.jigsawlibrary.jigsaw.controllers.JigsawDto;
import com.ceaa.jigsawlibrary.jigsaw.controllers.SaveJigsawRequestDto;
import com.ceaa.jigsawlibrary.jigsaw.domain.JigsawNotFoundException;
import com.ceaa.jigsawlibrary.jigsaw.repositories.JigsawDocument;
import com.ceaa.jigsawlibrary.jigsaw.repositories.JigsawMongoDataSource;
import com.ceaa.jigsawlibrary.shared.controllers.Error;
import com.ceaa.jigsawlibrary.shared.controllers.ErrorCode;
import com.ceaa.jigsawlibrary.shared.controllers.ValidationExceptionsAdvice;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.modelmapper.ModelMapper;
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
    private ModelMapper mapper;

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port + "/jigsaws").build();
        mapper = new ModelMapper();
    }

    @AfterEach
    void tearDown() {
        dataSource.deleteAll().block();
    }

    @Test
    void whenGettingJigsawWithNonAlphanumericIdShouldReturnBadRequest() {
        String invalidId = "invalid_id";
        client.get().uri("/" + invalidId).exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Error.class)
                .consumeWith(result -> {
                    assertThat(result.getResponseBody()).isNotNull();
                    assertThat(result.getResponseBody().code()).isEqualTo(ErrorCode.BAD_REQUEST);
                    assertThat(result.getResponseBody().message()).isEqualTo(ValidationExceptionsAdvice.ERROR_MESSAGE);
                    assertThat(result.getResponseBody().details()).isNotNull();
                    assertThat(result.getResponseBody().details().size()).isEqualTo(1);
                    assertThat(result.getResponseBody().details()).containsEntry("id", "must be alphanumerical");
                });
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
                .expectBody(JigsawDto.class)
                .consumeWith(result -> {
                    assertThat(result.getResponseBody()).isNotNull();
                    assertThat(result.getResponseBody())
                            .isEqualTo(mapper.map(storedJigsaw, JigsawDto.class));
                });
    }

    @Test
    void whenGettingJigsawsAndNoneAreStoredListShouldReturnEmptyList() {
        client.get().exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(JigsawDto.class)
                .hasSize(0);
    }

    @Test
    void getAllJigsaws() {
        List<JigsawDto> storedJigsaws = Flux.range(1,3)
                .map(i -> JigsawDocument.builder()
                        .title("title" + i).brand("brand" + i).shape("shape" + i).nPieces(500 * i)
                        .build())
                .flatMap(dataSource::save)
                .map(document -> mapper.map(document, JigsawDto.class))
                .collectList()
                .block();

        client.get().exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(JigsawDto.class)
                .consumeWith(result -> {
                    assertThat(result.getResponseBody()).isNotNull();
                    assertThat(result.getResponseBody()).hasSameSizeAs(storedJigsaws);
                    assertThat(result.getResponseBody()).hasSameElementsAs(storedJigsaws);
                });
    }

    @Test
    void createNewJigsaw() {
        SaveJigsawRequestDto jigsawToCreate = SaveJigsawRequestDto.builder()
                .title("title").brand("brand").subtitle("subtitle").collection("collection")
                .shape("shape").nPieces(500)
                .build();

        EntityExchangeResult<JigsawDto> result = client.post()
                .bodyValue(jigsawToCreate)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isCreated()
                .expectBody(JigsawDto.class)
                .returnResult();

        assertThat(result.getResponseBody()).isNotNull();
        JigsawDto returnedJigsaw = result.getResponseBody();
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
    void whenCreatingNewJigsawWithoutAllRequiredFields(ImmutablePair<SaveJigsawRequestDto, Map<String, String>> testData) {
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

    private static Stream<ImmutablePair<SaveJigsawRequestDto, Map<String, String>>> whenCreatingNewJigsawWithoutAllRequiredFields() {
        final String blankFieldMessage = "must not be blank";
        final String nullFieldMessage = "must not be null";
        final String notPositiveFieldMessage = "must be greater than 0";
        return Stream.of(
                new ImmutablePair<>(SaveJigsawRequestDto.builder().build(),
                        Map.of("title", blankFieldMessage, "brand", blankFieldMessage,
                                "shape", blankFieldMessage, "nPieces", nullFieldMessage)),
                new ImmutablePair<>(SaveJigsawRequestDto.builder().brand("brand").shape("shape").nPieces(500).build(),
                        Map.of("title", blankFieldMessage)),
                new ImmutablePair<>(SaveJigsawRequestDto.builder().title("title").shape("shape").nPieces(500).build(),
                        Map.of("brand", blankFieldMessage)),
                new ImmutablePair<>(SaveJigsawRequestDto.builder().title("title").brand("brand").nPieces(500).build(),
                        Map.of("shape", blankFieldMessage)),
                new ImmutablePair<>(SaveJigsawRequestDto.builder().title("title").brand("brand").shape("shape").build(),
                        Map.of("nPieces", nullFieldMessage)),
                new ImmutablePair<>(SaveJigsawRequestDto.builder().title("title").brand("brand").shape("shape").nPieces(0).build(),
                        Map.of("nPieces", notPositiveFieldMessage)),
                new ImmutablePair<>(SaveJigsawRequestDto.builder().title("title").brand("brand").shape("shape").nPieces(-1).build(),
                        Map.of("nPieces", notPositiveFieldMessage))
        );
    }
}
