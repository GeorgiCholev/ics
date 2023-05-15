package com.example.ics.models.dtos.image;

import com.example.ics.models.entities.Image;
import com.example.ics.models.entities.Tag;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ReadImageDto {

    private static final String DATE_FORMAT = "dd:MM:yyyy HH:mm";

    private final String id;

    private final String url;

    private final String analysedAt;

    private final Map<String, Integer> tags;

    private final Integer width;

    private final Integer height;

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
}
