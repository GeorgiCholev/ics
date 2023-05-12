package com.example.ics.services.impl;

import com.example.ics.models.dtos.ImageDto;
import com.example.ics.models.dtos.TagDto;
import com.example.ics.models.entities.Image;
import com.example.ics.models.entities.Tag;
import com.example.ics.repositories.ImageRepository;
import com.example.ics.repositories.TagRepository;
import com.example.ics.services.DataAccessService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    public void persist(ImageDto imageDto, List<TagDto> relatedTags) {
        List<Tag> tagsToPersist =
                relatedTags.stream()
                        .map(Tag::new)
                        .toList();
        imageRepository.saveAndFlush(new Image(imageDto, tagsToPersist));
    }

    @Override
    @Transactional
    public ImageDto getImageByUrl(String url) {
        Optional<Image> optImage = imageRepository.findByUrl(url);
        if (optImage.isEmpty()) {
            return null;
        }

        Image image = optImage.get();
        Set<TagDto> tagDtos = transformTagsToDtos(image.getTags());

        return new ImageDto(image, tagDtos);
    }

    @Override
    @Transactional
    public void update(ImageDto imageDto, List<TagDto> tagDtos) {
        Set<Tag> oldTags = transformDtosToTags(imageDto.getTags());
        tagRepository.deleteAll(oldTags);

        Set<Tag> newTags = transformDtosToTags(tagDtos);
        imageRepository.saveAndFlush(new Image(imageDto, newTags));
    }

    private static Set<Tag> transformDtosToTags(Collection<TagDto> dtos) {
        return dtos
                .stream()
                .map(Tag::new)
                .collect(Collectors.toSet());
    }
    private static Set<TagDto> transformTagsToDtos(Collection<Tag> tags) {
        return tags
                .stream()
                .map(TagDto::new)
                .collect(Collectors.toSet());
    }

}
