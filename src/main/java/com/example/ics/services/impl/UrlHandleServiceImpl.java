package com.example.ics.services.impl;

import com.example.ics.exceptions.ImageNotFoundException;
import com.example.ics.exceptions.InvalidImageUrlException;
import com.example.ics.exceptions.MishandledApiCallException;
import com.example.ics.models.dtos.image.ReadImageDto;
import com.example.ics.models.dtos.tag.ImaggaResultDto;
import com.example.ics.models.dtos.tag.TagsContainerDto;
import com.example.ics.models.dtos.image.PersistImageDto;
import com.example.ics.models.dtos.image.UpdateImageDto;
import com.example.ics.services.DataAccessService;
import com.example.ics.services.UrlHandleService;
import com.example.ics.credentials.ImaggaCredentials;
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

import static com.example.ics.exceptions.exception_handlers.ExceptionMessage.*;

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
    public ReadImageDto resolveTagsFrom(String url, boolean noCache)
            throws MishandledApiCallException, InvalidImageUrlException, ImageNotFoundException {

        ReadImageDto imageToResolve = dataAccessService.getImageForReadByUrl(url);

        if (imageToResolve != null && !noCache) {
            return imageToResolve;
        }

        PersistImageDto imageToPersist = null;
        if (imageToResolve == null) {
            imageToPersist = readImageFromUrl(url);
        }

        String jsonResponse = callCategorisationService(url);
        TagsContainerDto tagsContainerDto = readTagsFrom(jsonResponse);

        if (imageToResolve == null) {
            imageToResolve = dataAccessService.persist(imageToPersist, tagsContainerDto.getTags());
        } else {
            imageToResolve = dataAccessService.update(imageToResolve, tagsContainerDto.getTags());
        }

        return imageToResolve;
    }


    //    START-NOSCAN
    private PersistImageDto readImageFromUrl(String url) throws InvalidImageUrlException {
        try {
            URL urlObj = new URL(url);
            BufferedImage bufferedImage = ImageIO.read(urlObj);

            if (bufferedImage == null) {
                throw new InvalidImageUrlException(NOT_IMAGE_URL);
            }

            int height = bufferedImage.getHeight();
            int width = bufferedImage.getWidth();

            return new PersistImageDto(url, width, height);
        } catch (IOException e) {
            throw new InvalidImageUrlException(NOT_IMAGE_URL);
        }
    }
    // END-NOSCAN

    private TagsContainerDto readTagsFrom(String json) throws MishandledApiCallException {
        ObjectMapper objectMapper = new ObjectMapper();

        ImaggaResultDto resultObj;
        try {
            resultObj = objectMapper.readValue(json, ImaggaResultDto.class);
        } catch (JsonProcessingException e) {
            throw new MishandledApiCallException(NOT_CATEGORIZABLE_IMAGE);
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
                throw new MishandledApiCallException(NOT_CATEGORIZABLE_IMAGE);
            }

            BufferedReader connectionInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            jsonResponse = connectionInput.readLine();

            connectionInput.close();

        } catch (IOException e) {
            throw new MishandledApiCallException(NOT_CATEGORIZABLE_IMAGE);
        }

        return jsonResponse;
    }
    // END-NOSCAN
}
