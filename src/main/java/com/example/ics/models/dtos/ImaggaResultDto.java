package com.example.ics.models.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.stream.Collectors;

@JsonIgnoreProperties({"status"})
public class ImaggaResultDto {

    @JsonProperty("result")
    private TagsContainerDto result;

    private void filterMostConfidentTags() {
        result.setTags(
                result.getTags().stream()
                        .limit(3)
                        .toList()
        );
    }

    public TagsContainerDto getResult() {
        if (result.getTags().size() > 3) {
            filterMostConfidentTags();
        }
        return result;
    }

    public void setResult(TagsContainerDto result) {
        this.result = result;
    }
}
