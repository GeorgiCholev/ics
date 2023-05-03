package com.example.ics.models.dtos;

import java.util.List;

public class TagsContainerRespDto {
    private List<TagRespDto> tags;

    public List<TagRespDto> getTags() {
        return tags;
    }

    public void setTags(List<TagRespDto> tags) {
        this.tags = tags;
    }
}
