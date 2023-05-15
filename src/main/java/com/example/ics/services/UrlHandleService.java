package com.example.ics.services;

import com.example.ics.exceptions.InvalidImageUrlException;
import com.example.ics.exceptions.MishandledApiCallException;
import com.example.ics.models.dtos.tag.TagsContainerDto;

public interface UrlHandleService {

    TagsContainerDto resolveTagsFrom(String url, boolean noCache)
            throws MishandledApiCallException, InvalidImageUrlException;
}
