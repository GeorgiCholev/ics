package com.example.ics.services.impl;

import com.example.ics.models.dtos.tag.TagDto;
import com.example.ics.models.dtos.image.PersistImageDto;
import com.example.ics.models.dtos.image.UpdateImageDto;
import com.example.ics.models.entities.Image;
import com.example.ics.models.entities.Tag;
import com.example.ics.repositories.ImageRepository;
import com.example.ics.repositories.TagRepository;
import com.example.ics.services.DataAccessService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class DataAccessServiceImpl implements DataAccessService {

    private final ImageRepository imageRepository;

    private final TagRepository tagRepository;

    public DataAccessServiceImpl(ImageRepository imageRepository, TagRepository tagRepository) {
        this.imageRepository = imageRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional
    public UpdateImageDto getImageForUpdateByUrl(String url) {
        Image image = getImageByUrl(url);
        if (image == null) {
            return null;
        }

        Set<TagDto> tagDtos = transformTagEntitiesToDtos(image.getTags());
        return new UpdateImageDto(image, tagDtos);
    }

    @Override
    @Transactional
    public void persist(PersistImageDto imageDto, Set<TagDto> relatedTagDtos) {
        Set<Tag> tagEntities = transformTagDtosToEntities(relatedTagDtos);
        imageRepository.saveAndFlush(new Image(imageDto, tagEntities));
    }


    @Override
    @Transactional
    public void update(UpdateImageDto imageDto, Set<TagDto> relatedTagDtos) {
        Set<Tag> oldTags = transformTagDtosToEntities(imageDto.getTags());
        tagRepository.deleteAll(oldTags);

        Set<Tag> newTags = transformTagDtosToEntities(relatedTagDtos);
        imageRepository.saveAndFlush(new Image(imageDto, newTags));
    }

    private Image getImageByUrl(String url) {
        return imageRepository.findByUrl(url).orElse(null);
    }

    private Set<Tag> transformTagDtosToEntities(Set<TagDto> tagDtos) {
        return tagDtos
                .stream()
                .map(Tag::new)
                .collect(Collectors.toCollection(TreeSet::new));
    }
    private Set<TagDto> transformTagEntitiesToDtos(Set<Tag> tagEntities) {
        return tagEntities
                .stream()
                .map(TagDto::new)
                .collect(Collectors.toCollection(TreeSet::new));
    }

}
