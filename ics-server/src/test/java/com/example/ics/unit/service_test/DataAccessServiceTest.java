package com.example.ics.unit.service_test;

import com.example.ics.exceptions.ImageNotFoundException;
import com.example.ics.unit.models.EntitiesForTest;
import com.example.ics.models.dtos.image.ImageDto;
import com.example.ics.models.dtos.tag.TagDto;
import com.example.ics.models.entities.Image;
import com.example.ics.repositories.ImageRepository;
import com.example.ics.services.DataAccessService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
 class DataAccessServiceTest {

    @Autowired
    private DataAccessService dataAccessService;

    @MockBean
    private ImageRepository imageRepository;

    private EntitiesForTest entitiesForTest = new EntitiesForTest("validUrl", 0, 0, "checksum",
            new TagDto("1", 100),
            new TagDto("2", 50),
            new TagDto("3", 25),
            new TagDto("4", 12),
            new TagDto("5", 6)
            );

    private EntitiesForTest secondEntitiesForTest = new EntitiesForTest("otherUrl", 0, 0, "secondChecksum",
            new TagDto("6", 64),
            new TagDto("7", 32),
            new TagDto("8", 16),
            new TagDto("9", 8),
            new TagDto("10", 4)
    );


    @Test
    @DisplayName("getImageForUpdate - Not Found")
     void testGetImageForUpdateNotFound_ReturnsNull() {
        doReturn(Optional.empty()).when(imageRepository).findByUrl("notFoundUrl");

        ImageDto notFound = dataAccessService.getImageForReadByUrl("notFoundUrl");

        assertNull(notFound);
    }

    @Test
    @DisplayName("getImageForUpdate - Found")
     void testGetImageForUpdateFound_ReturnsImageUpdateDto() {
        Image image = entitiesForTest.getImage();

        doReturn(Optional.of(image)).when(imageRepository).findByUrl("validUrl");

        ImageDto found = dataAccessService.getImageForReadByUrl("validUrl");

        assertEquals(image.getUrl(), found.getUrl());
        assertEquals(image.getId(), found.getId());
    }

    @Test
    @DisplayName("Successful persist")
     void testPersistSavesEntity() {
        doReturn(Optional.of(entitiesForTest.getImage()))
                .when(imageRepository)
                .findByUrl("validUrl");

        dataAccessService.persist(entitiesForTest.getPersistImageDto(), entitiesForTest.getRelatedTagDtos());

        verify(imageRepository, times(1)).saveAndFlush(any(Image.class));
    }

    @Test
    @DisplayName("Successful update")
     void testUpdate_UpdatesSuccessfully() throws ImageNotFoundException {
        doReturn(Optional.of(entitiesForTest.getImage()))
                .when(imageRepository)
                        .findById("validId");
        dataAccessService.updateTagsById(entitiesForTest.getReadImageDto().getId(), secondEntitiesForTest.getRelatedTagDtos());

        verify(imageRepository, times(1)).saveAndFlush(any(Image.class));
    }

    @Test
    @DisplayName("getImageForReadById - Found")
     void testGetImageForReadById_ReturnsReadImageDto() throws ImageNotFoundException {
        doReturn(Optional.of(entitiesForTest.getImage()))
                .when(imageRepository)
                .findById("validId");

        ImageDto found = dataAccessService.getImageForReadById("validId");

        assertEquals(entitiesForTest.getReadImageDto().getUrl(), found.getUrl());
    }

    @Test
    @DisplayName("getImageForReadById - Not Found")
     void testGetImageForReadById_Throws() {
        doReturn(Optional.empty())
                .when(imageRepository)
                .findById("invalidId");

        assertThrows(ImageNotFoundException.class,
                () -> dataAccessService.getImageForReadById("invalidId"));
    }

    @Test
    @DisplayName("getImagesForRead by query parameters for page manipulation")
     void testGetImagesForRead_WithPageManipulation() throws ImageNotFoundException {
        List<ImageDto> expected = List.of(entitiesForTest.getReadImageDto());

        Page<Image> pageOfImages = new PageImpl<>(List.of(entitiesForTest.getImage()));
        doReturn(pageOfImages)
                .when(imageRepository)
                .findAll(any(Pageable.class));

        List<ImageDto> found =
                dataAccessService.getPageOfImagesForReadBy(false, 1, 1, null);

        assertEquals(expected.size(), found.size());
        assertEquals(expected.get(0).getUrl(), found.get(0).getUrl());
        assertEquals(expected.get(0).getTags().size(), found.get(0).getTags().size());
    }

    @Test
    @DisplayName("getImagesForRead by common tags")
     void testGetImagesForRead_WIthCommonTags() throws ImageNotFoundException {
        List<ImageDto> expected = List.of(entitiesForTest.getReadImageDto());

        Page<Image> pageOfImages = new PageImpl<>(List.of(entitiesForTest.getImage()));
        doReturn(pageOfImages)
                .when(imageRepository)
                .findAllThatContain(List.of("1", "2"),
                        PageRequest.of(0, 1, Sort.by("analysedAt")));

        List<ImageDto> found =
                dataAccessService.getPageOfImagesForReadBy(true, 0, 1, List.of("1", "2"));

        assertEquals(expected.size(), found.size());
        assertEquals(expected.get(0).getUrl(), found.get(0).getUrl());
        assertEquals(expected.get(0).getTags().size(), found.get(0).getTags().size());
    }
}
