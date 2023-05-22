package com.example.ics.models.dtos.image;

import com.example.ics.models.dtos.tag.TagDto;
import com.example.ics.models.entities.Image;

import java.util.Collections;
import java.util.Set;

public class UpdateImageDto {

    private final String id;

    private final String url;

    private final Integer width;

    private final Integer height;

    private final Set<TagDto> tags;

    public UpdateImageDto(Image entity, Set<TagDto> tagDtos) {
        this.id = entity.getId();
        this.url = entity.getUrl();
        this.width = entity.getWidth();
        this.height = entity.getHeight();
        this.tags = tagDtos;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public Set<TagDto> getTags() {
        return Collections.unmodifiableSet(tags);
    }
}
