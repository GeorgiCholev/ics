package com.example.ics.rest_assured;

import com.example.ics.models.dtos.image.ImageDto;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static com.example.ics.exceptions.exception_handlers.ExceptionMessage.*;
import static com.example.ics.rest_assured.TestArguments.BASE_URL;
import static com.example.ics.rest_assured.TestArguments.PATH;
import static com.example.ics.rest_assured.TestArguments.*;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ImagesPostTests {

    private static RequestSpecification requestSpecification;
    private static String idOfNewlyCreatedImage;


    @BeforeAll
    static void setUp() {

        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setBasePath(PATH)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("X-BYPASS-THROTTLE-KEY", System.getenv("BYPASS_THROTTLE_KEY"))
                .build();
    }

    @AfterAll
    static void tearDown() {
        deleteCreatedIssues();
    }

    private static void deleteCreatedIssues() {
        given()
                .spec(requestSpecification)
                .header("X-DELETE-KEY", System.getenv("DELETE_KEY"))
                .when()
                .delete("/" + idOfNewlyCreatedImage);
    }

    @Test
    @Order(1)
    @DisplayName("Post not present url --returns 201")
    void testPostForNotPresentUrl_Returns201() {
//        doReturn(true).when(bypassThrottle).preHandle(any(), any(), any());

        ImageDto newlyCreated =
                given()
                        .spec(requestSpecification)
                        .body(getJson(NEW_URL))
                        .when()
                        .post()
                        .prettyPeek()
                        .then()
                        .assertThat()
                        .statusCode(201)
                        .body(matchesJsonSchema(new File(IMAGE_JSON_TEMPLATE_PATH)))
                        .and()
                        .body("url", is(NEW_URL))
                        .body("analysedAt", not(emptyOrNullString()))
                        .body("width", not(emptyOrNullString()))
                        .body("height", not(emptyOrNullString()))
                        .and()
                        .extract().as(ImageDto.class);

        idOfNewlyCreatedImage = newlyCreated.getId();
    }

    @Test
    @Order(2)
    @DisplayName("Post for present url --returns 200")
    void testPostForPresentUrl_Returns200() {
//        doReturn(true).when(bypassThrottle).preHandle(any(), any(), any());

        given()
                .spec(requestSpecification)
                .body(getJson(NEW_URL))
                .when()
                .post()
                .prettyPeek()
                .then()
                .assertThat()
                .statusCode(200)
                .body(matchesJsonSchema(new File(IMAGE_JSON_TEMPLATE_PATH)))
                .and()
                .body("url", is(NEW_URL))
                .body("analysedAt", not(emptyOrNullString()))
                .body("width", not(emptyOrNullString()))
                .body("height", not(emptyOrNullString()))
                .and()
                .extract().as(ImageDto.class);
    }

    @Test
    @Order(3)
    @DisplayName("Post for present url and noCache active --returns 201")
    void testPostForPresentUrlWithNoCache_Returns200() {
//        doReturn(true).when(bypassThrottle).preHandle(any(), any(), any());

        given()
                .spec(requestSpecification)
                .body(getJson(NEW_URL))
                .queryParam("noCache", true)
                .when()
                .post()
                .prettyPeek()
                .then()
                .assertThat()
                .statusCode(201)
                .body(matchesJsonSchema(new File(IMAGE_JSON_TEMPLATE_PATH)))
                .and()
                .body("url", is(NEW_URL))
                .body("analysedAt", not(emptyOrNullString()))
                .body("width", not(emptyOrNullString()))
                .body("height", not(emptyOrNullString()));

    }

    @ParameterizedTest
    @ValueSource(strings = {NOT_VALID_URL, NOT_VALID_URL_2, NOT_VALID_URL_3, NOT_VALID_URL_4})
    @DisplayName("Post for not valid url --returns 400")
    void testPostWithInvalidUrls_Returns400(String url) {
//        doReturn(true).when(bypassThrottle).preHandle(any(), any(), any());

        given()
                .spec(requestSpecification)
                .body(getJson(url))
                .when()
                .post()
                .prettyPeek()
                .then()
                .assertThat()
                .statusCode(400)
                .body(matchesJsonSchema(new File(ERROR_JSON_TEMPLATE_PATH)))
                .and()
                .body("error", is(NOT_STANDARD_URL));

    }

    @ParameterizedTest
    @ValueSource(strings = {NOT_IMG_URL, NOT_IMG_URL_2, NOT_IMG_URL_3})
    @DisplayName("Post for not image url --returns 400")
    void testPostWithNotImageUrls_Returns400(String url) {
//        doReturn(true).when(bypassThrottle).preHandle(any(), any(), any());

        given()
                .spec(requestSpecification)
                .body(getJson(url))
                .when()
                .post()
                .prettyPeek()
                .then()
                .assertThat()
                .statusCode(400)
                .body(matchesJsonSchema(new File(ERROR_JSON_TEMPLATE_PATH)))
                .and()
                .body("error", is(NOT_IMAGE_URL));
    }

    @Test
    @DisplayName("Post for not categorizable image url --returns 503")
    void testPostWithNotCategorizableImageUrls_Returns503() {
//        doReturn(true).when(bypassThrottle).preHandle(any(), any(), any());

        given()
                .spec(requestSpecification)
                .body(getJson(NOT_CATEGORIZABLE_IMG_URL))
                .when()
                .post()
                .prettyPeek()
                .then()
                .assertThat()
                .statusCode(503)
                .body(matchesJsonSchema(new File(ERROR_JSON_TEMPLATE_PATH)))
                .and()
                .body("error", is(NOT_CATEGORIZABLE_IMAGE));
    }


    private String getJson(String url) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", url);
        return jsonObject.toJSONString();
    }
}
