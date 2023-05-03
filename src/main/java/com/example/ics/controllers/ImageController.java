package com.example.ics.controllers;

import com.example.ics.models.dtos.ImageAddressDto;
import com.example.ics.models.dtos.TagsContainerRespDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
public class ImageController {

    @PostMapping
    public ResponseEntity<TagsContainerRespDto> postImageAddress
            (@Valid @RequestBody final ImageAddressDto addressDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new TagsContainerRespDto(), HttpStatus.OK);
    }
}
