package com.example.ics.services;

import com.example.ics.models.dtos.tag.TagDto;
import com.example.ics.models.dtos.image.PersistImageDto;
import com.example.ics.models.dtos.image.UpdateImageDto;

import java.util.Set;

public interface DataAccessService {

    UpdateImageDto getImageForUpdateByUrl(String url);
    void persist(PersistImageDto imageDto, Set<TagDto> relatedTagDtos);

    void update(UpdateImageDto imageDto, Set<TagDto> newTagDtos);
}
