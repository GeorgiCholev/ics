package com.example.ics.services.impl;

import com.example.ics.exceptions.ImageNotFoundException;
import com.example.ics.exceptions.exception_handlers.ExceptionMessage;
import com.example.ics.models.dtos.image.OriginType;
import com.example.ics.models.dtos.image.ReadImageDto;
import com.example.ics.models.dtos.tag.TagDto;
import com.example.ics.models.dtos.image.PersistImageDto;
import com.example.ics.models.dtos.image.UpdateImageDto;
import com.example.ics.models.entities.Image;
import com.example.ics.models.entities.Tag;
import com.example.ics.repositories.ImageRepository;
import com.example.ics.repositories.TagRepository;
import com.example.ics.services.DataAccessService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

import static com.example.ics.exceptions.exception_handlers.ExceptionMessage.*;

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
    public ReadImageDto getImageForReadByUrl(String imageUrl) {
        Image image = getImageByUrl(imageUrl);
        if (image == null) {
            return null;
        }

        return new ReadImageDto(image, OriginType.FETCHED);
    }

    @Override
    @Transactional
    public ReadImageDto persist(PersistImageDto imageDto, Set<TagDto> relatedTagDtos) {
        Set<Tag> tagEntities = transformTagDtosToEntities(relatedTagDtos);
        imageRepository.saveAndFlush(new Image(imageDto, tagEntities));
        return new ReadImageDto(getImageByUrl(imageDto.url()), OriginType.CREATED);
    }


    @Override
    @Transactional
    public ReadImageDto update(ReadImageDto imageDto, Set<TagDto> relatedTagDtos) throws ImageNotFoundException {
        Image imageForUpdate = imageRepository.findByUrl(imageDto.getUrl())
                .orElseThrow(() -> new ImageNotFoundException(NOT_FOUND_IMAGE));

        Set<Tag> oldTags = imageForUpdate.getTags();
        tagRepository.deleteAll(oldTags);

        Set<Tag> newTags = transformTagDtosToEntities(relatedTagDtos);

        imageForUpdate.setTags(newTags);
        imageForUpdate.setAnalysedAt();

        imageRepository.saveAndFlush(imageForUpdate);

        return new ReadImageDto(imageForUpdate, OriginType.CREATED);
    }

    @Override
    @Transactional

    public ReadImageDto getImageForReadById(String imageId) throws ImageNotFoundException {
        Optional<Image> optImage = imageRepository.findById(imageId);
        if (optImage.isEmpty()) {
            throw new ImageNotFoundException(NOT_FOUND_IMAGE);
        }

        return new ReadImageDto(optImage.get(), OriginType.FETCHED);
    }

    @Override
    @Transactional
    public List<ReadImageDto> getPageOfImagesForReadBy
            (boolean ascOrder, int pageNum, int pageSize, List<String> tagNames) {

        Pageable pageable = getImagePageOf(ascOrder, pageNum, pageSize);
        Page<Image> page;
        if (tagNames != null) {
            page = imageRepository.findAllThatContain(tagNames, pageable);
        } else {
            page = imageRepository.findAll(pageable);
        }

        return getImagesForReadOutOf(page);
    }


    private List<ReadImageDto> getImagesForReadOutOf(Page<Image> page) {
        return page.stream()
                .map(entity -> new ReadImageDto(entity, OriginType.CREATED))
                .toList();
    }

    private PageRequest getImagePageOf(boolean ascOrder, int pageNum, int pageSize) {
        Sort sortBy;
        if (ascOrder) {
            sortBy = Sort.by("analysedAt");
        } else {
            sortBy = Sort.by(Sort.Order.desc("analysedAt"));
        }

        return PageRequest.of(pageNum, pageSize, sortBy);
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

    private Set<Tag> transformTagMapToEntities(Map<String, Integer> mapOfTags) {
        return mapOfTags.entrySet()
                .stream()
                .map(e -> new Tag(e.getKey(), e.getValue()))
                .collect(Collectors.toCollection(TreeSet::new));
    }

}
