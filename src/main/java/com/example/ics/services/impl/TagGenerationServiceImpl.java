package com.example.ics.services.impl;

import com.example.ics.exceptions.mishandledApiCallException.MishandledApiCallException;
import com.example.ics.models.dtos.ImageAddressDto;
import com.example.ics.models.dtos.TagsContainerRespDto;
import com.example.ics.services.TagGenerationService;
import com.example.ics.utils.ImaggaCredentials;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

@Service
public class TagGenerationServiceImpl implements TagGenerationService {

    private static final String IMAGGA_ENDPOINT_URL_FORMAT = "https://api.imagga.com/v2/tags?image_url=%s";
    private final ImaggaCredentials credentials;

    public TagGenerationServiceImpl(ImaggaCredentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public TagsContainerRespDto generateTags(ImageAddressDto imageAddressDto) {

        String finalUrl = String.format(IMAGGA_ENDPOINT_URL_FORMAT, imageAddressDto.getAddress());
        try {
            String jsonResponse = getTagsJson(finalUrl);
        } catch (MishandledApiCallException e) {
            return null;
        }

        return null;
    }

    private String getTagsJson(String finalUrl) throws MishandledApiCallException {

        Optional<String> optJson = Optional.empty();
        try {
            URL url = new URL(finalUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization", "Basic " + credentials.getEncodedCredentials());

            if (connection.getResponseCode() >= 400) {
                throw new MishandledApiCallException();
            }

            BufferedReader connectionInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            optJson = Optional.of(connectionInput.readLine());

            connectionInput.close();
        } catch (IOException e) {
            throw new MishandledApiCallException();
        }

        return optJson.orElseThrow(MishandledApiCallException::new);
    }
}
