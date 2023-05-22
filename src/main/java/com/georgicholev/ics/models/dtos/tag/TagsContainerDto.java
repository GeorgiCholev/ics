package com.example.ics.models.dtos.tag;

import java.util.*;

public class TagsContainerDto {
    private Set<TagDto> tags;

    public TagsContainerDto() {

    }

    public TagsContainerDto(Set<TagDto> tags) {
        this.tags = tags;
    }

    public Set<TagDto> getTags() {
        return tags;
    }

    public void setTags(Set<TagDto> tags) {
        this.tags = new TreeSet<>(tags);
    }
}
