package com.example.ics.models.dtos;

public class ExceptionMessageDto {

    private String error;

    public ExceptionMessageDto(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
