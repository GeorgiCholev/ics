package com.example.ics.rest_assured;

import com.example.ics.controllers.ImageController;
import com.example.ics.models.dtos.image.ImageDto;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.example.ics.exceptions.exception_handlers.ExceptionMessage.*;
import static com.example.ics.rest_assured.TestArguments.*;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ImagesGetAllTests {

    private static final int PAGE_SIZE = Integer.parseInt(ImageController.DEFAULT_PAGE_SIZE);
    private static RequestSpecification requestSpecification;

    @BeforeAll
    static void setUp() {
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setBasePath(PATH)
                .build();
    }

    @Test
    @DisplayName("Get all images --returns 200")
    void testGetAllImages_Returns200() {

        given()
                .spec(requestSpecification)
        .when()
                .get()
                .prettyPeek()
        .then()
                .assertThat()
                .statusCode(200)
                .body(matchesJsonSchema(new File(ALL_IMAGES_JSON_TEMPLATE_PATH)))
                .body("", hasSize(PAGE_SIZE));
    }

    @Test
    @DisplayName("Get all images in ASC")
    void testGetAllImagesInAsc() {
        ImageDto[] imagesInAsc =
                given()
                        .spec(requestSpecification)
                        .queryParam("ascOrder", true)
                .when()
                        .get().as(ImageDto[].class);

        assertTrue(areInAscOrder(imagesInAsc));
    }

    private boolean areInAscOrder(ImageDto[] imagesInAsc) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        for (int i = 0; i < imagesInAsc.length - 1; i++) {
            LocalDateTime current = LocalDateTime.parse(imagesInAsc[i].getAnalysedAt(), formatter);
            LocalDateTime next = LocalDateTime.parse(imagesInAsc[i + 1].getAnalysedAt(), formatter);

            if (current.compareTo(next) > 0) {
                return false;
            }
        }
        return true;
    }

    @Test
    @DisplayName("Get different pages returns different results")
    void testGetDifferentPages_ReturnDifferentResults() {
        ImageDto[] firstPage =
                given()
                        .spec(requestSpecification)
                        .queryParam("num", 0)
                .when()
                        .get().as(ImageDto[].class);

        ImageDto[] secondPage =
                given()
                        .spec(requestSpecification)
                        .queryParam("num", 1)
                .when()
                        .get().as(ImageDto[].class);

        assertEquals(firstPage.length, secondPage.length);
        assertTrue(haveDifferentImages(firstPage, secondPage));
        assertTrue(areInDescOrder(firstPage, secondPage));
    }

    private boolean areInDescOrder(ImageDto[] first, ImageDto[] second) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime firstOldest = LocalDateTime.parse(first[first.length - 1].getAnalysedAt(), formatter);
        LocalDateTime secondLatest = LocalDateTime.parse(second[0].getAnalysedAt(), formatter);
        return firstOldest.compareTo(secondLatest) >= 0;
    }

    private boolean haveDifferentImages(ImageDto[] first, ImageDto[] second) {
        for (int i = 0; i < first.length; i++) {
            if (first[i].getId().equals(second[i].getId())) {
                return false;
            }
        }
        return true;
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5, 7, 10})
    @DisplayName("Get different size of images by query")
    void testGetDifferentSizeOfImagesByQuery(int size) {
        given()
                .spec(requestSpecification)
                .queryParam("size", size)
        .when()
                .get()
                .prettyPeek()
        .then()
                .assertThat()
                .statusCode(200)
                .body("", hasSize(size));
    }

    @ParameterizedTest
    @CsvSource({"-1, 1", "0, 11", "-1, 0", "string, string"})
    @DisplayName("Get images with invalid query parameters returns 400")
    void testGetImagesWithInvalidQueryParameters(String num, String size) {
        given()
                .spec(requestSpecification)
                .queryParam("num", num)
                .queryParam("size", size)
        .when()
                .get()
                .prettyPeek()
        .then()
                .assertThat()
                .statusCode(400)
                .body("error", is(NOT_ACCEPTED_REQUEST));
    }

    @ParameterizedTest
    @CsvSource({"horse, invalidTag", "horse, cat", "kitten, person"})
    @DisplayName("Get images by tags only specified")
    void testGetImagesWithTags_ReturnSpecified(String tagOne, String tagTwo) {
        List<ImageDto> receivedImages =
                given()
                    .spec(requestSpecification)
                    .queryParam("tag", tagOne, tagTwo)
                .when()
                    .get()
                    .prettyPeek()
                .then()
                    .assertThat()
                    .statusCode(200)
                    .and()
                    .body(matchesJsonSchema(new File(ALL_IMAGES_JSON_TEMPLATE_PATH)))
                    .and()
                    .extract().jsonPath()
                    .getList(".", ImageDto.class);

        assertTrue(everyImageHasAtLeastOneOf(receivedImages, tagOne, tagTwo));
    }

    private boolean everyImageHasAtLeastOneOf(List<ImageDto> receivedImages, String... tags) {
        for (ImageDto img : receivedImages) {
            if (Arrays.stream(tags).noneMatch(t -> img.getTags().containsKey(t))) {
                return false;
            }
        }
        return true;
    }


    static Stream<List<String>> variableInvalidTagsSize() {
        return Stream.of(
                List.of("invalidTag"),
                List.of("invalidTag", "secondInvalidTag"),
                List.of("123", "###", "ТагИме")
        );
    }
    @ParameterizedTest
    @MethodSource("variableInvalidTagsSize")
    @DisplayName("Get images by tags with invalid arguments")
    void testGetImagesByTagsWithInvalidArguments_Returns204(List<String> tags) {
        given()
                .spec(requestSpecification)
                .queryParam("tag", tags)
        .when()
                .get()
                .prettyPeek()
        .then()
                .assertThat()
                .statusCode(204);

    }
}
