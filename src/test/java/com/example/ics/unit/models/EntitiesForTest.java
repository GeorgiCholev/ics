package com.example.ics.unit.models;

import com.example.ics.models.dtos.image.PersistImageDto;
import com.example.ics.models.dtos.image.ReadImageDto;
import com.example.ics.models.dtos.image.UpdateImageDto;
import com.example.ics.models.dtos.tag.TagDto;
import com.example.ics.models.entities.Image;
import com.example.ics.models.entities.Tag;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class EntitiesForTest {

    private UpdateImageDto updateImageDto;
    private PersistImageDto persistImageDto;

    private ReadImageDto readImageDto;
    private Image image;

    private Set<Tag> relatedTags;
    private Set<TagDto> relatedTagDtos;

    public EntitiesForTest(String url, int width, int height, TagDto... tagDtos) {
        createTags(tagDtos);
        this.persistImageDto = new PersistImageDto(url, width, height);
        this.image = new Image(persistImageDto, relatedTags);
        this.updateImageDto = new UpdateImageDto(image, relatedTagDtos);
        this.readImageDto = new ReadImageDto(image);
    }

    private void createTags(TagDto[] tagDtos) {
        this.relatedTagDtos = Set.of(tagDtos);


        this.relatedTags = this.relatedTagDtos.stream()
                .map(Tag::new)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public UpdateImageDto getUpdateImageDto() {
        return updateImageDto;
    }

    public PersistImageDto getPersistImageDto() {
        return persistImageDto;
    }

    public Image getImage() {
        return image;
    }

    public Set<Tag> getRelatedTags() {
        return relatedTags;
    }

    public Set<TagDto> getRelatedTagDtos() {
        return relatedTagDtos;
    }

    public ReadImageDto getReadImageDto() {
        return readImageDto;
    }
}
