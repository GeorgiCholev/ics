package com.example.ics.services;

import com.example.ics.exceptions.InvalidImageUrlException;
import com.example.ics.exceptions.MishandledApiCallException;
import com.example.ics.models.dtos.TagsContainerDto;

public interface TagGenerationService {

    TagsContainerDto generateTagsFor(String address) throws MishandledApiCallException, InvalidImageUrlException;
}
