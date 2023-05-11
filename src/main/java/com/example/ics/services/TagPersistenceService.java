package com.example.ics.services;

import com.example.ics.models.dtos.TagDto;
import com.example.ics.models.entities.Tag;

import java.util.List;

public interface TagPersistenceService {
    List<Tag> persistAll(List<TagDto> tags);
}
