package com.example.ics.controllers;

import com.example.ics.exceptions.InvalidImageUrlException;
import com.example.ics.exceptions.MishandledApiCallException;
import com.example.ics.models.dtos.ImageAddressDto;
import com.example.ics.models.dtos.TagsContainerDto;
import com.example.ics.services.TagGenerationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final TagGenerationService tagGenerationService;

    public ImageController(TagGenerationService tagGenerationService) {
        this.tagGenerationService = tagGenerationService;
    }

    @PostMapping
    public ResponseEntity<TagsContainerDto> postImageAddress(@RequestBody final ImageAddressDto addressDto)
            throws MishandledApiCallException, InvalidImageUrlException {

        final TagsContainerDto tagsContainerDto = tagGenerationService.generateTagsFor(addressDto.getAddress());

        return new ResponseEntity<>(tagsContainerDto, HttpStatus.OK);
    }
}
