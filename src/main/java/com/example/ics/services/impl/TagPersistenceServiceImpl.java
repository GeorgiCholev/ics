package com.example.ics.services.impl;

import com.example.ics.models.dtos.TagDto;
import com.example.ics.models.entities.Tag;
import com.example.ics.repositories.TagRepository;
import com.example.ics.services.TagPersistenceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagPersistenceServiceImpl implements TagPersistenceService {

    private final TagRepository tagRepository;

    public TagPersistenceServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> persistAll(List<TagDto> tags) {
        return tagRepository.saveAllAndFlush(
                tags.stream()
                        .map(Tag::new)
                        .collect(Collectors.toList()));
    }
}
