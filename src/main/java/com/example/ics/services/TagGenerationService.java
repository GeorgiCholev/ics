package com.example.ics.services;

import com.example.ics.models.dtos.ImageAddressDto;
import com.example.ics.models.dtos.TagsContainerRespDto;

public interface TagGenerationService {

    TagsContainerRespDto generateTags(ImageAddressDto imageAddressDto);
}
