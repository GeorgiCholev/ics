package com.example.ics.services.impl;

import com.example.ics.exceptions.InvalidImageUrlException;
import com.example.ics.exceptions.MishandledApiCallException;
import com.example.ics.models.dtos.ImaggaResultDto;
import com.example.ics.models.dtos.TagsContainerDto;
import com.example.ics.services.TagGenerationService;
import com.example.ics.utilities.ImaggaCredentials;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class TagGenerationServiceImpl implements TagGenerationService {

    private static final String IMAGGA_ENDPOINT_URL_FORMAT = "https://api.imagga.com/v2/tags?image_url=%s&limit=%d";
    private static final int TAG_LIMIT = 3;
    private final ImaggaCredentials credentials;

    @Autowired
    public TagGenerationServiceImpl(ImaggaCredentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public TagsContainerDto generateTagsFor(String address)
            throws MishandledApiCallException, InvalidImageUrlException {

        validateImageAddress(address);

        String jsonResponse = callCategorisationService(address);

        TagsContainerDto tagsContainerDto = resolveTagsFrom(jsonResponse);

        return tagsContainerDto;
    }

    private void validateImageAddress(String address) throws InvalidImageUrlException {
        try {
            URL url = new URL(address);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("HEAD");
            int responseCode = urlConnection.getResponseCode();

            if (responseCode < 200 || responseCode >= 400) {
                throw new InvalidImageUrlException();
            }

            String contentType = urlConnection.getContentType();

            if (contentType == null || !contentType.startsWith("image/")) {
                throw new InvalidImageUrlException();
            }
        } catch (IOException e) {
            throw new InvalidImageUrlException();
        }
    }

    private TagsContainerDto resolveTagsFrom(String json) throws MishandledApiCallException {
        ObjectMapper objectMapper = new ObjectMapper();

        ImaggaResultDto resultObj;
        try {
            resultObj = objectMapper.readValue(json, ImaggaResultDto.class);
        } catch (JsonProcessingException e) {
            throw new MishandledApiCallException();
        }

        return resultObj.getResult();
    }

    private String callCategorisationService(String address) throws MishandledApiCallException {
//      https://api.imagga.com/v2/tags?image_url=https://i.ytimg.com/vi/8AlUJHh7Fxw/maxresdefault.jpg&limit=5
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
}
