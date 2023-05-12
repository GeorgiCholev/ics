package com.example.ics.services;

import com.example.ics.models.dtos.ImageDto;
import com.example.ics.models.dtos.TagDto;

import java.util.List;

public interface DataAccessService {
    void persist(ImageDto imageDto, List<TagDto> relatedTags);

    ImageDto getImageByUrl(String url);

    void update(ImageDto imageDto, List<TagDto> tagDtos);
}
