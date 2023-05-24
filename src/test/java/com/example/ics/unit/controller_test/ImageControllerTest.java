package com.example.ics.unit.controller_test;

import com.example.ics.exceptions.ImageNotFoundException;
import com.example.ics.exceptions.MishandledApiCallException;
import com.example.ics.models.dtos.image.OriginType;
import com.example.ics.models.dtos.image.ImageDto;
import com.example.ics.models.dtos.tag.TagDto;
import com.example.ics.services.DataAccessService;
import com.example.ics.services.UrlHandleService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.TreeMap;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ImageControllerTest {

    @MockBean
    private UrlHandleService urlHandleService;

    @MockBean
    private DataAccessService dataAccessService;

    @Autowired
    private MockMvc mockMvc;

    private static final String URL = "https://a-z-animals.com/media/horse-3.jpg";
    private static ImageDto mockedImage;
    private static ImageDto specificMockedImage;

    @BeforeAll
    public static void setUp() {
        setUpMockImage();
        setUpSpecificMockImage();
    }

    private static void setUpSpecificMockImage() {
        TreeMap<String, Integer> mappedTags = new TreeMap<>();
        mappedTags.put("validTag", 50);
        mappedTags.put("specificTag", 50);
        specificMockedImage =
                new ImageDto("validId2", "validUrl2", "14-01-2023 10:38", mappedTags, 0, 0, OriginType.CREATED);
    }

    private static void setUpMockImage() {
        TreeMap<String, Integer> mappedTags = new TreeMap<>();
        TagDto mockedTag = new TagDto("validTag", 100);
        mappedTags.put("validTag", 100);
        mockedImage =
                new ImageDto("validId", "validUrl", "14-05-2023 10:38", mappedTags, 0, 0, OriginType.CREATED);
    }

    private ResultActions requestAndExpectStatus(MockHttpServletRequestBuilder request, ResultMatcher status)
            throws Exception {
        return mockMvc.perform(request)
                .andExpect(status)
                .andDo(print());
    }

    @Test
    @DisplayName("GET /images/{id} - Found")
    public void testGetImageByIdFound() throws Exception {
        doReturn(mockedImage).when(dataAccessService).getImageForReadById("validId");

        requestAndExpectStatus(get("/images/{id}", "validId"), status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", is("validId")))
                .andExpect(jsonPath("$.url").value("validUrl"))
                .andExpect(jsonPath("$.analysedAt").value("14-05-2023 10:38"))
                .andExpect(jsonPath("$.width").value(0))
                .andExpect(jsonPath("$.height").value(0));
    }

    @Test
    @DisplayName("GET /images/{id} - Not Found")
    public void testGetImageByIdNotFound() throws Exception {
        doThrow(ImageNotFoundException.class).when(dataAccessService).getImageForReadById("invalidId");

        requestAndExpectStatus(get("/images/{id}", "invalidId"), status().isNoContent());
    }

    @Test
    @DisplayName("GET /images - Get all images")
    public void testGetImagesReturnsAllInDesc() throws Exception {
        doReturn(List.of(mockedImage, specificMockedImage)).when(dataAccessService)
                .getPageOfImagesForReadBy(false, 0, 5, null);

        requestAndExpectStatus(get("/images"), status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].tags.validTag", is(100)))
                .andExpect(jsonPath("$[1].tags.validTag", is(50)))
                .andExpect(jsonPath("$[1].tags.specificTag", is(50)));
    }

    @Test
    @DisplayName("GET /images - Get all images by tag in ASC")
    public void testGetImagesByTagAndAsc_ReturnsAllWithTag() throws Exception {
        doReturn(List.of(specificMockedImage, mockedImage)).when(dataAccessService)
                .getPageOfImagesForReadBy(true, 0, 5, List.of("validTag", "invalidTag"));

        requestAndExpectStatus(get("/images?tag=validTag&tag=invalidTag&ascOrder=true"), status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].tags.validTag", is(50)))
                .andExpect(jsonPath("$[0].tags.specificTag", is(50)))
                .andExpect(jsonPath("$[1].tags.validTag", is(100)));
    }

    @Test
    @DisplayName("GET /images - Get all images with non existent tag")
    public void testGetImagesByNonExistentTag_ReturnsEmpty() throws Exception {
        doReturn(List.of()).when(dataAccessService)
                .getPageOfImagesForReadBy(false, 0, 5, List.of("nonExistentTag"));

        requestAndExpectStatus(get("/images?tag=nonExistentTag"), status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("GET /images - Get all images with page number and page size")
    public void testGetImagesByPageNumberANdPageSizeReturnOneImage() throws Exception {
        doReturn(List.of(specificMockedImage)).when(dataAccessService)
                .getPageOfImagesForReadBy(false, 1, 1, null);

        requestAndExpectStatus(get("/images?num=1&size=1"), status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("validId2")))
                .andExpect(jsonPath("$[0].tags.validTag", is(50)))
                .andExpect(jsonPath("$[0].tags.specificTag", is(50)));
    }

    @Test
    @DisplayName("GET /images - Invalid query parameters")
    public void testGetImagesWithInvalidQueryParameters_ThrowsExc() throws Exception {
        requestAndExpectStatus(get("/images?num=-1&size=0"), status().isBadRequest());
    }

    @Test
    @DisplayName("POST /images - Valid URL")
    public void testPostImagesWithValidUrlReturnsTags() throws Exception {

        doReturn(mockedImage)
                .when(urlHandleService)
                .resolveTagsFrom(URL, false);

        requestAndExpectStatus(post("/images")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body(URL)), status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", is("validId")))
                .andExpect(jsonPath("$.url").value("validUrl"));
    }

    @Test
    @DisplayName("POST /images - Invalid url")
    public void testPostImagesWithInvalidUrl_ReturnsBadRequest() throws Exception {
        requestAndExpectStatus(post("/images")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body("invalidUrl")), status().isBadRequest());
    }

    @Test
    @DisplayName("POST /images - Unrecognizable url")
    public void testPostImagesWithUnrecognisableUrl_ReturnServiceUnavailable() throws Exception {
        doThrow(MishandledApiCallException.class).when(urlHandleService)
                .resolveTagsFrom(URL, false);

        requestAndExpectStatus(post("/images")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body(URL)), status().isServiceUnavailable());
    }

    private String body(String url) {
        return "{\"url\": \"" + url + "\"}";
    }

}
