package com.example.ics.unit.models;

import com.example.ics.models.dtos.image.OriginType;
import com.example.ics.models.dtos.image.PersistImageDto;
import com.example.ics.models.dtos.image.ImageDto;
import com.example.ics.models.dtos.tag.TagDto;
import com.example.ics.models.entities.Image;
import com.example.ics.models.entities.Tag;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class EntitiesForTest {

    private PersistImageDto persistImageDto;

    private ImageDto imageDto;
    private Image image;

    private Set<Tag> relatedTags;
    private Set<TagDto> relatedTagDtos;

    public EntitiesForTest(String url, int width, int height, String checksum, TagDto... tagDtos) {
        createTags(tagDtos);
        this.persistImageDto = new PersistImageDto(url, width, height, checksum);
        this.image = new Image(persistImageDto, relatedTags);
        image.setId("validId");
        this.imageDto = new ImageDto(image, OriginType.CREATED);
    }

    private void createTags(TagDto[] tagDtos) {
        this.relatedTagDtos = Set.of(tagDtos);


        this.relatedTags = this.relatedTagDtos.stream()
                .map(Tag::new)
                .collect(Collectors.toCollection(TreeSet::new));
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

    public ImageDto getReadImageDto() {
        return imageDto;
    }
}
