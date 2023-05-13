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
}
