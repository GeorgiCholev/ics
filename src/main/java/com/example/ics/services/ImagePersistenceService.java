package com.example.ics.services;

import com.example.ics.models.dtos.ImageDto;
import com.example.ics.models.entities.Tag;

import java.util.List;

public interface ImagePersistenceService {
    void persist(ImageDto imageDto, List<Tag> relatedTags);
}
