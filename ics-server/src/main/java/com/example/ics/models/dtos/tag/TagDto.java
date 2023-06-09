package com.example.ics.models.dtos.tag;

import com.example.ics.models.entities.Tag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class TagDto implements Comparable<TagDto> {

    @JsonIgnore
    private String id;
    private String name;
    private int confidence;

    public TagDto() {
    }

    public TagDto(Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
        this.confidence = tag.getConfidence();
    }

    public TagDto(String name, int confidence) {
        this.name = name;
        this.confidence = confidence;
    }

    @JsonProperty("tag")
    public void setNameFromJson(Map<String, String> tagFields) {
        this.name = tagFields.get("en");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = (int) Math.round(confidence);
    }

    @Override
    public int compareTo(TagDto o) {
        int orderByConfidence = Integer.compare(o.getConfidence(), this.confidence);
        return orderByConfidence != 0 ? orderByConfidence : this.name.compareTo(o.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagDto tagDto = (TagDto) o;

        if (confidence != tagDto.confidence) return false;
        return name.equals(tagDto.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + confidence;
        return result;
    }
}
