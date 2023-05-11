package com.example.ics.services.impl;

import com.example.ics.models.dtos.ImageDto;
import com.example.ics.models.entities.Image;
import com.example.ics.models.entities.Tag;
import com.example.ics.repositories.ImageRepository;
import com.example.ics.services.ImagePersistenceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImagePersistenceServiceImpl implements ImagePersistenceService {

    private final ImageRepository imageRepository;

    public ImagePersistenceServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public void persist(ImageDto imageDto, List<Tag> relatedTags) {
        imageRepository.saveAndFlush(new Image(imageDto, relatedTags));
    }
}
