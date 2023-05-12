package com.example.ics.models.dtos;

import com.example.ics.models.entities.Image;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;

public class ImageDto {

    private String id;
    private String url;

    private LocalDateTime analysedAt;

    private Set<TagDto> tags;
    private Integer width;

    private Integer height;

    public ImageDto() {
        this.tags = new TreeSet<>();
    }

    public ImageDto(String url, Integer width, Integer height) {
        this();
        this.url = url;
        this.width = width;
        this.height = height;
    }

    public ImageDto(Image image, Set<TagDto> tags) {
        this.id = image.getId();
        this.url = image.getUrl();
        this.analysedAt = image.getAnalysedAt();
        this.tags = tags;
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getAnalysedAt() {
        return analysedAt;
    }

    public void setAnalysedAt(LocalDateTime analysedAt) {
        this.analysedAt = analysedAt;
    }

    public Set<TagDto> getTags() {
        return tags;
    }

    public void setTags(Set<TagDto> tags) {
        this.tags = tags;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
