package com.example.ics.services;

import com.example.ics.exceptions.ImageNotFoundException;
import com.example.ics.exceptions.InvalidImageUrlException;
import com.example.ics.exceptions.MishandledApiCallException;
import com.example.ics.models.dtos.image.ImageDto;

public interface UrlHandleService {

    ImageDto resolveTagsFrom(String url, boolean noCache)
            throws MishandledApiCallException, InvalidImageUrlException, ImageNotFoundException;

}
