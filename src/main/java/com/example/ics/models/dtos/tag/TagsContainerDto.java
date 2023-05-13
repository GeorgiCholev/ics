package com.example.ics.models.dtos.tag;

import java.util.*;

public class TagsContainerDto {
    private Set<TagDto> tags;

    public TagsContainerDto() {
        this.tags = new TreeSet<>();
    }

    public TagsContainerDto(Set<TagDto> tags) {
        this.tags = tags;
    }

    public Set<TagDto> getTags() {
        return tags;
    }

    public void setTags(TreeSet<TagDto> tags) {
        this.tags = tags;
    }
}
