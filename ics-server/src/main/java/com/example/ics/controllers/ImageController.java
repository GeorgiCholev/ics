package com.example.ics.controllers;

import com.example.ics.exceptions.ImageNotFoundException;
import com.example.ics.exceptions.InvalidImageUrlException;
import com.example.ics.exceptions.MishandledApiCallException;
import com.example.ics.models.dtos.UrlDto;
import com.example.ics.models.dtos.image.ImageDto;
import com.example.ics.services.DataAccessService;
import com.example.ics.services.UrlHandleService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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

    public static final String DEFAULT_PAGE_SIZE = "5";
    private final UrlHandleService urlHandleService;
    private final DataAccessService dataAccessService;

    public ImageController(UrlHandleService urlHandleService, DataAccessService dataAccessService) {
        this.urlHandleService = urlHandleService;
        this.dataAccessService = dataAccessService;
    }

    @GetMapping
    public ResponseEntity<List<ImageDto>> getAllImages(
            @RequestParam(value = "tag", required = false) List<String> tagNames,
            @RequestParam(required = false) boolean ascOrder,
            @RequestParam(defaultValue = "0") @Min(0) int num,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) @Min(1) @Max(10) int size)
            throws ImageNotFoundException {

        List<ImageDto> images = dataAccessService.getPageOfImagesForReadBy(ascOrder, num, size, tagNames);

        return new ResponseEntity<>(images, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageDto> getImageById(@PathVariable("id") @NotBlank String imageId)
            throws ImageNotFoundException {

        ImageDto image = dataAccessService.getImageForReadById(imageId);

        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    //    START-NOSCAN
    @PostMapping
    public ResponseEntity<ImageDto> postImageUrl(
            @RequestBody @Validated final UrlDto urlDto, BindingResult bindingResult,
            @RequestParam(required = false) boolean noCache
    ) throws MishandledApiCallException, InvalidImageUrlException, ImageNotFoundException {

        if (bindingResult.hasErrors()) {
            throw new InvalidImageUrlException(NOT_STANDARD_URL);
        }

        final ImageDto resolvedImage = urlHandleService.resolveTagsFrom(urlDto.getUrl(), noCache);

        return new ResponseEntity<>(resolvedImage, resolvedImage.getOriginStatus());
    }
    //    END-NOSCAN


    public static final String DELETE_KEY = System.getenv("DELETE_KEY");

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteImage(
            @PathVariable @NotBlank String id,
            @RequestHeader("X-DELETE-KEY") String providedDeleteKey) {

        if (DELETE_KEY == null || !DELETE_KEY.equals(providedDeleteKey)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        dataAccessService.deleteImage(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
