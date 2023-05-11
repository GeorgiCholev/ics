package com.example.ics.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class TagDto {
    private String name;
    private int confidence;

    @JsonProperty("tag")
    public void setNameFromJson(Map<String, String> tagFields) {
        this.name = tagFields.get("en");
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

}
