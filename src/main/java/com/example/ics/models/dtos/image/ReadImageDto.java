package com.example.ics.models.dtos.image;

import com.example.ics.models.entities.Image;
import com.example.ics.models.entities.Tag;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ReadImageDto {

    private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm";

    private String id;

    private String url;

    private String analysedAt;

    private Map<String, Integer> tags;

    private Integer width;

    private Integer height;

    public ReadImageDto(Image entity) {
        this.id = entity.getId();
        this.url = entity.getUrl();
        this.analysedAt = entity.getAnalysedAt().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        this.tags = entity.getTags().stream()
                .collect(
                        Collectors.toMap((Tag::getName), (Tag::getConfidence), ((t1, t2) -> t1), (LinkedHashMap::new))
                );
        this.width = entity.getWidth();
        this.height = entity.getHeight();
    }

    public ReadImageDto(String id, String url, String analysedAt, Map<String, Integer> tags, Integer width, Integer height) {
        this.id = id;
        this.url = url;
        this.analysedAt = analysedAt;
        this.tags = tags;
        this.width = width;
        this.height = height;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getAnalysedAt() {
        return analysedAt;
    }

    public Map<String, Integer> getTags() {
        return Collections.unmodifiableMap(this.tags);
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAnalysedAt(String analysedAt) {
        this.analysedAt = analysedAt;
    }

    public void setTags(Map<String, Integer> tags) {
        this.tags = tags;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
