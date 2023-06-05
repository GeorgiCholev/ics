package com.example.ics.services.impl;

import com.example.ics.exceptions.ImageNotFoundException;
import com.example.ics.models.dtos.image.OriginType;
import com.example.ics.models.dtos.image.ImageDto;
import com.example.ics.models.dtos.tag.TagDto;
import com.example.ics.models.dtos.image.PersistImageDto;
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
    public ImageDto getImageForReadByUrl(String imageUrl) {
        Image image = getImageByUrl(imageUrl);
        if (image == null) {
            return null;
        }

        return new ImageDto(image, OriginType.FETCHED);
    }

    @Override
    @Transactional
    public ImageDto getImageForReadById(String imageId) throws ImageNotFoundException {
        Image image = getImageById(imageId);
        if (image == null) {
            throw new ImageNotFoundException();
        }

        return new ImageDto(image, OriginType.FETCHED);
    }

    @Override
    @Transactional
    public ImageDto getImageForReadByChecksum(String checksum) {
        Optional<Image> optImage = imageRepository.findByChecksum(checksum);

        if (optImage.isEmpty()) {
            return null;
        }

        return new ImageDto(optImage.get(), OriginType.FETCHED);
    }

    @Override
    @Transactional
    public ImageDto persist(PersistImageDto imageDto, Set<TagDto> relatedTagDtos) {
        Set<Tag> tagEntities = transformTagDtosToEntities(relatedTagDtos);
        imageRepository.saveAndFlush(new Image(imageDto, tagEntities));
        return new ImageDto(getImageByUrl(imageDto.url()), OriginType.CREATED);
    }


    @Override
    @Transactional
    public ImageDto updateTagsById(String imageId, Set<TagDto> newTagDtos) {
        Image imageForUpdate = getImageById(imageId);

        if (imageForUpdate == null) {
            return new ImageDto();
        }

        Set<Tag> oldTags = imageForUpdate.getTags();
        tagRepository.deleteAll(oldTags);

        Set<Tag> newTags = transformTagDtosToEntities(newTagDtos);

        imageForUpdate.updateTags(newTags);

        imageRepository.saveAndFlush(imageForUpdate);

        return new ImageDto(imageForUpdate, OriginType.CREATED);
    }


    @Override
    @Transactional
    public List<ImageDto> getPageOfImagesForReadBy
            (boolean ascOrder, int pageNum, int pageSize, List<String> tagNames)
            throws ImageNotFoundException {

        Pageable pageable = getImagePageOf(ascOrder, pageNum, pageSize);
        Page<Image> page;

        if (tagNames != null) {
            page = imageRepository.findAllThatContain(tagNames, pageable);
        } else {
            page = imageRepository.findAll(pageable);
        }

        if (page.isEmpty()) {
            throw new ImageNotFoundException();
        }

        return getImagesForReadOutOf(page);
    }

    @Override
    public void deleteImage(String id) {
        imageRepository.deleteById(id);
    }


    private List<ImageDto> getImagesForReadOutOf(Page<Image> page) {
        return page.stream()
                .map(entity -> new ImageDto(entity, OriginType.CREATED))
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

    private Image getImageById(String id) {
        return imageRepository.findById(id).orElse(null);
    }

    private Set<Tag> transformTagDtosToEntities(Set<TagDto> tagDtos) {
        return tagDtos
                .stream()
                .map(Tag::new)
                .collect(Collectors.toCollection(TreeSet::new));
    }

}
