package com.example.ics.models.dtos.image;

import org.springframework.http.HttpStatus;

public enum OriginType {

    FETCHED(HttpStatus.OK), CREATED(HttpStatus.CREATED);

    private final HttpStatus httpStatus;


    OriginType(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
