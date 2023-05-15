package com.example.ics.services;

import com.example.ics.exceptions.ImageNotFoundException;
import com.example.ics.models.dtos.image.ReadImageDto;
import com.example.ics.models.dtos.tag.TagDto;
import com.example.ics.models.dtos.image.PersistImageDto;
import com.example.ics.models.dtos.image.UpdateImageDto;

import java.util.List;
import java.util.Set;

public interface DataAccessService {

    UpdateImageDto getImageForUpdateByUrl(String imageUrl);

    void persist(PersistImageDto imageDto, Set<TagDto> relatedTagDtos);

    void update(UpdateImageDto imageDto, Set<TagDto> newTagDtos);

    ReadImageDto getImageForReadById(String imageId) throws ImageNotFoundException;

    List<ReadImageDto> getPageOfImagesForReadBy(boolean ascOrder, int pageNum, int pageSize, List<String> tagNames);

}
