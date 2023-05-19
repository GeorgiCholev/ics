package com.example.ics.models.dtos;


import com.example.ics.validation.ValidUrl;

public class UrlDto {
    @ValidUrl
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
