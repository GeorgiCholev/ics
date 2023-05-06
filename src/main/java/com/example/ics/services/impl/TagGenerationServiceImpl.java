package com.example.ics.services.impl;

import com.example.ics.exceptions.MishandledApiCallException;
import com.example.ics.models.dtos.ImageAddressDto;
import com.example.ics.models.dtos.ImaggaResultDto;
import com.example.ics.models.dtos.TagsContainerDto;
import com.example.ics.services.TagGenerationService;
import com.example.ics.utilities.ImaggaCredentials;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class TagGenerationServiceImpl implements TagGenerationService {

    private static final String IMAGGA_ENDPOINT_URL_FORMAT = "https://api.imagga.com/v2/tags?image_url=%s";
    private final ImaggaCredentials credentials;

    public TagGenerationServiceImpl(ImaggaCredentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public TagsContainerDto generateTags(ImageAddressDto imageAddressDto) throws MishandledApiCallException {

        String imaggaFinalUrl = String.format(IMAGGA_ENDPOINT_URL_FORMAT, imageAddressDto.getAddress());

        String jsonResponse = callCategorisationService(imaggaFinalUrl);

        TagsContainerDto tagsContainerDto = resolveTagsFrom(jsonResponse);

        return tagsContainerDto;
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

    private String callCategorisationService(String finalUrl) throws MishandledApiCallException {

        String jsonResponse;

        try {
            URL url = new URL(finalUrl);
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
