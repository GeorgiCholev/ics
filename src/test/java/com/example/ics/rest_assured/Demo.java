package com.example.ics.rest_assured;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
public class Demo {

    public static final String BASE_URL = "http://localhost:8080";

    @Test
    public void getBase() {
        RestAssured.get(BASE_URL)
                .prettyPeek()
                .then()
                .statusCode(lessThan(500));

    }
}
