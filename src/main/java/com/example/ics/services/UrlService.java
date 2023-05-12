package com.example.ics.services;

import com.example.ics.exceptions.InvalidImageUrlException;
import com.example.ics.exceptions.MishandledApiCallException;
import com.example.ics.models.dtos.TagsContainerDto;

public interface UrlService {

    TagsContainerDto resolveTagsFrom(String url, boolean noCache)
            throws MishandledApiCallException, InvalidImageUrlException;
}
