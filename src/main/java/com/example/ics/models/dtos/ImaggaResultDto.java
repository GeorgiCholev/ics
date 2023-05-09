package com.example.ics.models.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({"status"})
public class ImaggaResultDto {

    @JsonProperty("result")
    private TagsContainerDto result;

    public TagsContainerDto getResult() {
        return result;
    }

    public void setResult(TagsContainerDto result) {
        this.result = result;
    }
}
