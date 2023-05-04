package com.example.ics.services;

import com.example.ics.exceptions.MishandledApiCallException;
import com.example.ics.models.dtos.ImageAddressDto;
import com.example.ics.models.dtos.TagsContainerDto;

public interface TagGenerationService {

    TagsContainerDto generateTags(ImageAddressDto imageAddressDto) throws MishandledApiCallException;
}
