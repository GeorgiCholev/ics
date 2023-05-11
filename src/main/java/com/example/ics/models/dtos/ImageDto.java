package com.example.ics.models.dtos;

import jakarta.persistence.Column;

import java.time.LocalDateTime;
import java.util.List;

public class ImageDto {

    private final String url;

    private final LocalDateTime analysedAt;

    private final Integer width;

    private final Integer height;

    public ImageDto(String url, Integer width, Integer height) {
        this.url = url;
        this.analysedAt = LocalDateTime.now();
        this.width = width;
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public LocalDateTime getAnalysedAt() {
        return analysedAt;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }
}
