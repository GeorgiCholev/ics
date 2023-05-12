package com.example.ics.controllers;

import com.example.ics.exceptions.InvalidImageUrlException;
import com.example.ics.exceptions.MishandledApiCallException;
import com.example.ics.models.dtos.UrlDto;
import com.example.ics.models.dtos.TagsContainerDto;
import com.example.ics.services.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final UrlService urlService;

    public ImageController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping
    public ResponseEntity<TagsContainerDto> postImageUrl(
            @RequestBody final UrlDto urlDto, @RequestParam(required = false, defaultValue = "false") boolean noCache
    ) throws MishandledApiCallException, InvalidImageUrlException {

        final TagsContainerDto tagsContainerDto = urlService.resolveTagsFrom(urlDto.getUrl(), noCache);

        return new ResponseEntity<>(tagsContainerDto, HttpStatus.OK);
    }
}
