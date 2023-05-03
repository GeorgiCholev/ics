package com.example.ics.controllers;

import com.example.ics.models.dtos.ImageAddressDto;
import com.example.ics.models.dtos.TagsContainerRespDto;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
public class ImageController {

    @PostMapping
    public TagsContainerRespDto postImageAddress(@Valid @RequestBody final ImageAddressDto addressDto,
                                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            System.out.println("Invalid address");
        }

        System.out.println("Valid Address");
        return new TagsContainerRespDto();
    }
}
