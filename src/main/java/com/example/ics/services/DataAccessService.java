package com.example.ics.services;

import com.example.ics.exceptions.ImageNotFoundException;
import com.example.ics.models.dtos.image.ImageDto;
import com.example.ics.models.dtos.tag.TagDto;
import com.example.ics.models.dtos.image.PersistImageDto;

import java.util.List;
import java.util.Set;

public interface DataAccessService {

    ImageDto getImageForReadByUrl(String imageUrl);

    ImageDto getImageForReadById(String imageId) throws ImageNotFoundException;

    ImageDto getImageForReadByChecksum(String checksum);

    ImageDto persist(PersistImageDto imageDto, Set<TagDto> relatedTagDtos);
    ImageDto updateTagsById(String imageId, Set<TagDto> newTagDtos) throws ImageNotFoundException;
    List<ImageDto> getPageOfImagesForReadBy(boolean ascOrder, int pageNum, int pageSize, List<String> tagNames)
            throws ImageNotFoundException;


    void deleteImage(String id);

}
