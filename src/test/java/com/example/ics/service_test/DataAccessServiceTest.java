package com.example.ics.service_test;

import com.example.ics.exceptions.ImageNotFoundException;
import com.example.ics.exceptions.exception_handlers.ExceptionMessage;
import com.example.ics.models.EntitiesForTest;
import com.example.ics.models.dtos.image.ReadImageDto;
import com.example.ics.models.dtos.image.UpdateImageDto;
import com.example.ics.models.dtos.tag.TagDto;
import com.example.ics.models.entities.Image;
import com.example.ics.repositories.ImageRepository;
import com.example.ics.repositories.TagRepository;
import com.example.ics.services.DataAccessService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DataAccessServiceTest {

    @Autowired
    private DataAccessService dataAccessService;

    @MockBean
    private ImageRepository imageRepository;

    @MockBean
    private TagRepository tagRepository;

    private EntitiesForTest entitiesForTest = new EntitiesForTest("validUrl", 0, 0,
            new TagDto("1", 100),
            new TagDto("2", 50),
            new TagDto("3", 25),
            new TagDto("4", 12),
            new TagDto("5", 6)
            );

    private EntitiesForTest secondEntitiesForTest = new EntitiesForTest("otherUrl", 0, 0,
            new TagDto("6", 64),
            new TagDto("7", 32),
            new TagDto("8", 16),
            new TagDto("9", 8),
            new TagDto("10", 4)
    );


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

    @Test
    @DisplayName("Successful persist")
    public void testPersistSavesEntity() {
        dataAccessService.persist(entitiesForTest.getPersistImageDto(), entitiesForTest.getRelatedTagDtos());

        verify(imageRepository, times(1)).saveAndFlush(any(Image.class));
    }

    @Test
    @DisplayName("Successful update")
    public void testUpdate_UpdatesSuccessfully() {
        dataAccessService.update(entitiesForTest.getUpdateImageDto(), secondEntitiesForTest.getRelatedTagDtos());

        verify(tagRepository, times(1)).deleteAll(entitiesForTest.getRelatedTags());
        verify(imageRepository, times(1)).saveAndFlush(any(Image.class));
    }

    @Test
    @DisplayName("getImageForReadById - Found")
    public void testGetImageForReadById_ReturnsReadImageDto() throws ImageNotFoundException {
        doReturn(Optional.of(entitiesForTest.getImage()))
                .when(imageRepository)
                .findById("validId");

        ReadImageDto found = dataAccessService.getImageForReadById("validId");

        assertEquals(entitiesForTest.getReadImageDto().getUrl(), found.getUrl());
    }

    @Test
    @DisplayName("getImageForReadById - Not Found")
    public void testGetImageForReadById_Throws() {
        doReturn(Optional.empty())
                .when(imageRepository)
                .findById("invalidId");

        assertThrows(ImageNotFoundException.class,
                () -> dataAccessService.getImageForReadById("invalidId"),
                ExceptionMessage.NOT_FOUND_IMAGE);
    }

    @Test
    @DisplayName("getImagesForRead by query parameters for page manipulation")
    public void testGetImagesForRead_WithPageManipulation() {
        List<ReadImageDto> expected = List.of(entitiesForTest.getReadImageDto());

        Page<Image> pageOfImages = new PageImpl<>(List.of(entitiesForTest.getImage()));
        doReturn(pageOfImages)
                .when(imageRepository)
                .findAll(any(Pageable.class));

        List<ReadImageDto> found =
                dataAccessService.getPageOfImagesForReadBy(false, 1, 1, null);

        assertEquals(expected.size(), found.size());
        assertEquals(expected.get(0).getUrl(), found.get(0).getUrl());
        assertEquals(expected.get(0).getTags().size(), found.get(0).getTags().size());
    }

    @Test
    @DisplayName("getImagesForRead by common tags")
    public void testGetImagesForRead_WIthCommonTags() {
        List<ReadImageDto> expected = List.of(entitiesForTest.getReadImageDto());

        Page<Image> pageOfImages = new PageImpl<>(List.of(entitiesForTest.getImage()));
        doReturn(pageOfImages)
                .when(imageRepository)
                .findAllThatContain(List.of("1", "2"),
                        PageRequest.of(0, 1, Sort.by("analysedAt")));

        List<ReadImageDto> found =
                dataAccessService.getPageOfImagesForReadBy(true, 0, 1, List.of("1", "2"));

        assertEquals(expected.size(), found.size());
        assertEquals(expected.get(0).getUrl(), found.get(0).getUrl());
        assertEquals(expected.get(0).getTags().size(), found.get(0).getTags().size());
    }
}
