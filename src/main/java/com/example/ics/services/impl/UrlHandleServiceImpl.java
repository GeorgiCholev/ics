package com.example.ics.services.impl;

import com.example.ics.exceptions.InvalidImageUrlException;
import com.example.ics.exceptions.MishandledApiCallException;
import com.example.ics.models.dtos.tag.ImaggaResultDto;
import com.example.ics.models.dtos.tag.TagsContainerDto;
import com.example.ics.models.dtos.image.PersistImageDto;
import com.example.ics.models.dtos.image.UpdateImageDto;
import com.example.ics.services.DataAccessService;
import com.example.ics.services.UrlHandleService;
import com.example.ics.utilities.ImaggaCredentials;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class UrlHandleServiceImpl implements UrlHandleService {

    private static final String IMAGGA_ENDPOINT_URL_FORMAT = "https://api.imagga.com/v2/tags?image_url=%s&limit=%d";
    private static final int TAG_LIMIT = 5;
    private final ImaggaCredentials credentials;
    private final DataAccessService dataAccessService;


    @Autowired
    public UrlHandleServiceImpl(ImaggaCredentials credentials, DataAccessService dataAccessService) {
        this.credentials = credentials;
        this.dataAccessService = dataAccessService;
    }

    @Override
    public TagsContainerDto resolveTagsFrom(String url, boolean noCache)
            throws MishandledApiCallException, InvalidImageUrlException {

//        1. check DB
        UpdateImageDto imageFromDB = dataAccessService.getImageForUpdateByUrl(url);

//        2. if image exists and noCache is false  Exit {YesNo}
        if (imageFromDB != null && !noCache) {
            return new TagsContainerDto(imageFromDB.getTags());
        }

//        3. if image does not exist validate {NoYes, NoNo}
        PersistImageDto imageToPersist = null;
        if (imageFromDB == null) {
            imageToPersist = validateImageUrl(url);
        }

//        4. call categorisation service
        String jsonResponse = callCategorisationService(url);
        TagsContainerDto tagsContainerDto = readTagsFrom(jsonResponse);

//        5. if image does not exist -> save, else -> update
        if (imageFromDB == null) {
            dataAccessService.persist(imageToPersist, tagsContainerDto.getTags());
        } else {
            dataAccessService.update(imageFromDB, tagsContainerDto.getTags());
        }

        return tagsContainerDto;
    }

    @Override
    public void hello() {

    }

    //    START-NOSCAN
    private PersistImageDto validateImageUrl(String urlAddress) throws InvalidImageUrlException {
        try {
            URL url = new URL(urlAddress);
            BufferedImage bufferedImage = ImageIO.read(url);

            if (bufferedImage == null) {
                throw new InvalidImageUrlException();
            }

            int height = bufferedImage.getHeight();
            int width = bufferedImage.getWidth();

            return new PersistImageDto(urlAddress, width, height);
        } catch (IOException e) {
            throw new InvalidImageUrlException();
        }
    }
    // END-NOSCAN

    private TagsContainerDto readTagsFrom(String json) throws MishandledApiCallException {
        ObjectMapper objectMapper = new ObjectMapper();

        ImaggaResultDto resultObj;
        try {
            resultObj = objectMapper.readValue(json, ImaggaResultDto.class);
        } catch (JsonProcessingException e) {
            throw new MishandledApiCallException();
        }

        return resultObj.getResult();
    }

//    START-NOSCAN
    private String callCategorisationService(String address) throws MishandledApiCallException {
//      https://api.imagga.com/v2/tags?image_url=https://example.com/example.jpg&limit=5
        String imaggaFinalUrl = String.format(IMAGGA_ENDPOINT_URL_FORMAT, address, TAG_LIMIT);

        String jsonResponse;

        try {
            URL url = new URL(imaggaFinalUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization", "Basic " + credentials.getEncodedCredentials());

            if (connection.getResponseCode() >= 400) {
                throw new MishandledApiCallException();
            }

            BufferedReader connectionInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            jsonResponse = connectionInput.readLine();

            connectionInput.close();

        } catch (IOException e) {
            throw new MishandledApiCallException();
        }

        return jsonResponse;
    }
    // END-NOSCAN
}
