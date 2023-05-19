package com.example.ics.service_test;

import com.example.ics.models.dtos.image.UpdateImageDto;
import com.example.ics.models.entities.Image;
import com.example.ics.models.entities.Tag;
import com.example.ics.repositories.ImageRepository;
import com.example.ics.repositories.TagRepository;
import com.example.ics.services.DataAccessService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DataAccessServiceTest {

    @Autowired
    private DataAccessService dataAccessService;

    @MockBean
    private ImageRepository imageRepository;

    @MockBean
    private TagRepository tagRepository;

    private static Image mockImage;

    @BeforeAll
    public static void setUp() {

    }


    @Test
    @DisplayName("getImageForUpdate - Not Found")
    public void testGetImageForUpdateNotFound_ReturnsNull() {
        doReturn(Optional.empty()).when(imageRepository).findByUrl("notFoundUrl");

        UpdateImageDto notFound = dataAccessService.getImageForUpdateByUrl("notFoundUrl");

        assertNull(notFound);
    }

    @Test
    @DisplayName("getImageForUpdate - Found")
    public void testGetImageForUpdateFound_ReturnsImageUpdateDto() {
        Image image = new Image("validUrl", 0, 0, new TreeSet<>());

        doReturn(Optional.of(image)).when(imageRepository).findByUrl("validUrl");

        UpdateImageDto found = dataAccessService.getImageForUpdateByUrl("validUrl");

        assertEquals(image.getUrl(), found.getUrl());
        assertEquals(image.getId(), found.getId());
    }


}
