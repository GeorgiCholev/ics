package com.example.ics.models.dtos;

import java.util.*;
import java.util.stream.Collectors;

public class TagsContainerDto {
    private List<TagDto> tags;

    public TagsContainerDto() {
    }

    public TagsContainerDto(Collection<TagDto> tags) {
        this.tags = new ArrayList<>(tags);
    }

    public List<TagDto> getTags() {
        return tags;
    }

    public void setTags(List<TagDto> tags) {
        this.tags = tags;
    }
}
