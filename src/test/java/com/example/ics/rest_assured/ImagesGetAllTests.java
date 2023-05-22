package com.example.ics.rest_assured;

import com.example.ics.controllers.ImageController;
import com.example.ics.exceptions.exception_handlers.ExceptionMessage;
import com.example.ics.models.dtos.image.ReadImageDto;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;

import static com.example.ics.exceptions.exception_handlers.ExceptionMessage.*;
import static com.example.ics.rest_assured.TestArguments.*;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

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
        ReadImageDto[] imagesInAsc =
                given()
                        .spec(requestSpecification)
                        .queryParam("ascOrder", true)
                        .when()
                        .get().as(ReadImageDto[].class);

        assertTrue(areInAscOrder(imagesInAsc));
    }

    private boolean areInAscOrder(ReadImageDto[] imagesInAsc) {
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
        ReadImageDto[] firstPage =
                given()
                        .spec(requestSpecification)
                        .queryParam("num", 0)
                        .when()
                        .get().as(ReadImageDto[].class);

        ReadImageDto[] secondPage =
                given()
                        .spec(requestSpecification)
                        .queryParam("num", 1)
                        .when()
                        .get().as(ReadImageDto[].class);

        assertEquals(firstPage.length, secondPage.length);
        assertTrue(haveDifferentImages(firstPage, secondPage));
        assertTrue(areInDescOrder(firstPage, secondPage));
    }

    private boolean areInDescOrder(ReadImageDto[] first, ReadImageDto[] second) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime firstOldest = LocalDateTime.parse(first[first.length - 1].getAnalysedAt(), formatter);
        LocalDateTime secondLatest = LocalDateTime.parse(second[0].getAnalysedAt(), formatter);
        return firstOldest.compareTo(secondLatest) >= 0;
    }

    private boolean haveDifferentImages(ReadImageDto[] first, ReadImageDto[] second) {
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
    @CsvSource({"-1, 1", "0, 0", "-1, 0"})
    @DisplayName("Get images with invalid query parameters returns 400")
    void testGetImagesWithInvalidQueryParameters(int num, int size) {
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

//    @ParameterizedTest
//    @CsvSource({"horse, invalidTag", "horse, cat"})
//    @DisplayName("Get images by tags only specified")
//    void testGetImagesWithTags_ReturnSpecified(String tagOne, String tagTwo) {
//        given()
//                .spec(requestSpecification)
//                .queryParam("tag", tagOne, tagTwo)
//                .when()
//                .get()
//                .prettyPeek()
//                .then()
//                .assertThat()
//                .statusCode(200)
//                .and()
//                .body(matchesJsonSchema(new File(ALL_IMAGES_JSON_TEMPLATE_PATH)))
//                .and()
//                .body("$.tags", hasItem(atLeastOneOf(tagOne, tagTwo)));
//    }
//
////    TODO: fix matcher and test invalidTags
//
//    public static Matcher<Map<String, Integer>> atLeastOneOf(String... tagNames) {
//        return new BaseMatcher<>() {
//            @Override
//            public boolean matches(Object item) {
//                if (item instanceof Map<?, ?> map) {
//                    return Arrays.stream(tagNames).anyMatch(map::containsKey);
//                }
//                return false;
//            }
//
//            @Override
//            public void describeTo(Description description) {
//                description.appendText("Containing " + Arrays.toString(tagNames));
//            }
//        };
//    }
}
