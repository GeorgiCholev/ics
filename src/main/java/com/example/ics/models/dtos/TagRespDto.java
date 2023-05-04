package com.example.ics.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class TagRespDto {
    private String name;
    private double confidence;

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

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

}
