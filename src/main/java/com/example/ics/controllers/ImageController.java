package com.example.ics.controllers;

import com.example.ics.exceptions.ImageNotFoundException;
import com.example.ics.exceptions.InvalidImageUrlException;
import com.example.ics.exceptions.MishandledApiCallException;
import com.example.ics.models.dtos.UrlDto;
import com.example.ics.models.dtos.image.ReadImageDto;
import com.example.ics.models.dtos.tag.TagsContainerDto;
import com.example.ics.services.DataAccessService;
import com.example.ics.services.UrlHandleService;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.ics.exceptions.exception_handlers.ExceptionMessage.*;

@RestController
@Validated
@RequestMapping("/images")
public class ImageController {

    private final UrlHandleService urlHandleService;
    private final DataAccessService dataAccessService;

    public ImageController(UrlHandleService urlHandleService, DataAccessService dataAccessService) {
        this.urlHandleService = urlHandleService;
        this.dataAccessService = dataAccessService;
    }

    @GetMapping
    public ResponseEntity<List<ReadImageDto>> getAllImages(
            @RequestParam(value = "tag", required = false) List<String> tagNames,
            @RequestParam(required = false) boolean ascOrder,
            @RequestParam(defaultValue = "0") @Min(0) int num,
            @RequestParam(defaultValue = "5") @Min(1) int size) {

        List<ReadImageDto> images = dataAccessService.getPageOfImagesForReadBy(ascOrder, num, size, tagNames);

        return new ResponseEntity<>(images, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadImageDto> getImageById(@PathVariable("id") String imageId)
            throws ImageNotFoundException {

        ReadImageDto image = dataAccessService.getImageForReadById(imageId);

        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TagsContainerDto> postImageUrl(
            @RequestBody @Validated final UrlDto urlDto, BindingResult bindingResult,
            @RequestParam(required = false) boolean noCache
    ) throws MishandledApiCallException, InvalidImageUrlException {

        if (bindingResult.hasErrors()) {
            throw new InvalidImageUrlException(NOT_STANDARD_URL);
        }

        final TagsContainerDto tagsContainerDto = urlHandleService.resolveTagsFrom(urlDto.getUrl(), noCache);

        return new ResponseEntity<>(tagsContainerDto, HttpStatus.OK);
    }
}
