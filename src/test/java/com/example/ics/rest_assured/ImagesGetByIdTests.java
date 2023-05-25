package com.example.ics.rest_assured;

import com.example.ics.models.dtos.image.ImageDto;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.*;

import static com.example.ics.exceptions.exception_handlers.ExceptionMessage.*;
import static com.example.ics.rest_assured.TestArguments.*;
import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ImagesGetByIdTests {

    private static RequestSpecification requestSpecification;

    @BeforeAll
    static void setUp() {
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setBasePath(PATH_WITH_ID)
                .build();
    }

    @Test
    @DisplayName("Get with path variable --ValidId returns 200")
    void testGetWithId_Returns200() {
        given()
                .spec(requestSpecification)
                .pathParam("id", VALID_IMG_ID)
        .when()
                .get()
                 .prettyPeek()
        .then()
                .assertThat()
                 .statusCode(200)
                 .body(matchesJsonSchema(new File(IMAGE_JSON_TEMPLATE_PATH)))
                .and()
                 .body("url", is(VALID_URL))
                 .body("analysedAt", not(emptyOrNullString()))
                 .body("width", not(emptyOrNullString()))
                 .body("height", not(emptyOrNullString()));
    }

    @Test
    @DisplayName("Get with path variable --ValidId returns --validImage with sorted tags 200")
    void testGetWithId_ReturnsSortedTags() {
        ImageDto image =
                given()
                    .spec(requestSpecification)
                    .pathParam("id", VALID_IMG_ID)
                .when()
                    .get()
                        .as(ImageDto.class);

        assertEquals(VALID_URL, image.getUrl());
        Map<String, Integer> tags = image.getTags();
        assertEquals(5, tags.size());
        assertTrue(tagAreInDescOrder(new ArrayList<>(tags.values())));
    }

    private boolean tagAreInDescOrder(List<Integer> tagsConfidence) {
        for (int i = 1; i < tagsConfidence.size() - 1; i++) {
            if (tagsConfidence.get(i).compareTo(tagsConfidence.get(i + 1)) < 0) {
                return false;
            }
        }
        return true;
    }


    @ParameterizedTest
    @ValueSource(strings = {NOT_VALID_IMG_ID, VALID_IMG_ID + "; DROP TABLE some_table;--"})
    @DisplayName("Get with path variable --NotValid returns 204")
    void testGetWithNotValidId_Returns204(String id) {
        given()
                .spec(requestSpecification)
                .pathParam("id", id)
        .when()
                .get()
                .prettyPeek()
        .then()
                .assertThat()
                 .statusCode(204);
    }


    @Test
    @DisplayName("Get with path variable --Whitespace returns 400")
    void testGetWithWhitespaceId_Returns400() {
        given()
                .spec(requestSpecification)
                .pathParam("id", "   ")
        .when()
                .get()
                .prettyPeek()
        .then()
                .assertThat()
                 .statusCode(400)
                 .body(matchesJsonSchema(
                        new File(ERROR_JSON_TEMPLATE_PATH)))
                .and()
                 .body("error", is(NOT_ACCEPTED_REQUEST));
    }

}
