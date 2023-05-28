package com.example.ics.services.impl;

import com.example.ics.exceptions.ImageNotFoundException;
import com.example.ics.exceptions.InvalidImageUrlException;
import com.example.ics.exceptions.MishandledApiCallException;
import com.example.ics.models.dtos.image.ImageDto;
import com.example.ics.models.dtos.tag.ImaggaResultDto;
import com.example.ics.models.dtos.tag.TagDto;
import com.example.ics.models.dtos.image.PersistImageDto;
import com.example.ics.services.DataAccessService;
import com.example.ics.services.UrlHandleService;
import com.example.ics.credentials.ImaggaHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

import static com.example.ics.exceptions.exception_handlers.ExceptionMessage.*;

@Service
public class UrlHandleServiceImpl implements UrlHandleService {
    private static final int TAG_LIMIT = 5;
    private final ImaggaHandler imaggaHandler;
    private final DataAccessService dataAccessService;


    @Autowired
    public UrlHandleServiceImpl(ImaggaHandler imaggaHandler, DataAccessService dataAccessService) {
        this.imaggaHandler = imaggaHandler;
        this.dataAccessService = dataAccessService;
    }

    @Override
    public ImageDto resolveTagsFrom(String url, boolean noCache)
            throws MishandledApiCallException, InvalidImageUrlException, ImageNotFoundException {

        ImageDto imageToResolve = dataAccessService.getImageForReadByUrl(url);

        if (imageToResolve != null && !noCache) {
            return imageToResolve;
        }

        PersistImageDto imageToPersist = null;
        if (imageToResolve == null) {

            imageToPersist = readImageFromUrl(url);

            imageToResolve =
                    dataAccessService.getImageForReadByChecksum(imageToPersist.checksum());
            if (imageToResolve != null && !noCache) {
                return imageToResolve;
            }
        }

        String jsonResponse = callCategorisationService(url);
        Set<TagDto> generatedTags = readTagsFrom(jsonResponse);

        if (imageToResolve == null) {
            imageToResolve = dataAccessService.persist(imageToPersist, generatedTags);
        } else {
            imageToResolve = dataAccessService.updateTagsById(imageToResolve.getId(), generatedTags);
        }

        return imageToResolve;
    }


    //    START-NOSCAN
    private PersistImageDto readImageFromUrl(String url) throws InvalidImageUrlException {
        try {

            RestTemplate restTemplate = new RestTemplate();

            byte[] imageBytes = restTemplate.getForObject(url, byte[].class);
            if (imageBytes == null) {
                throw new RestClientException("");
            }

            BufferedImage bufferedImage = readImageBytes(imageBytes);
            int height = bufferedImage.getHeight();
            int width = bufferedImage.getWidth();

            String checksum = calculateChecksum(imageBytes);

            return new PersistImageDto(url, width, height, checksum);
        } catch (IOException | RestClientException | NoSuchAlgorithmException e) {
            throw new InvalidImageUrlException(NOT_IMAGE_URL);
        }
    }
    //    END-NOSCAN

    private static BufferedImage readImageBytes(byte[] imageBytes) throws IOException {
        ByteArrayInputStream streamForReadingImage = new ByteArrayInputStream(imageBytes);
        BufferedImage bufferedImage = ImageIO.read(streamForReadingImage);
        if (bufferedImage == null) {
            throw new IOException();
        }
        streamForReadingImage.close();
        return bufferedImage;
    }

    private static String calculateChecksum(byte[] imageBytes) throws NoSuchAlgorithmException, IOException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        InputStream streamForChecksum = new ByteArrayInputStream(imageBytes);

        byte[] nextBytesRead = new byte[1_024];
        int numberOfBytes;

        while ((numberOfBytes = streamForChecksum.read(nextBytesRead)) != -1) {
            messageDigest.update(nextBytesRead, 0, numberOfBytes);
        }
        streamForChecksum.close();

        byte[] digestBytes = messageDigest.digest();

        StringBuilder sb = new StringBuilder();

        for (byte digestByte : digestBytes) {
            sb.append(Integer.toString((digestByte & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    private Set<TagDto> readTagsFrom(String json) throws MishandledApiCallException {
        ObjectMapper objectMapper = new ObjectMapper();

        ImaggaResultDto resultObj;
        try {
            resultObj = objectMapper.readValue(json, ImaggaResultDto.class);
        } catch (JsonProcessingException e) {
            throw new MishandledApiCallException(NOT_CATEGORIZABLE_IMAGE);
        }

        return resultObj.getResult().getTags();
    }

    //    START-NOSCAN
    private String callCategorisationService(String address) throws MishandledApiCallException {
        String jsonResponse = imaggaHandler.call(address, TAG_LIMIT);
        if (jsonResponse == null) {
            throw new MishandledApiCallException(NOT_CATEGORIZABLE_IMAGE);
        }

        return jsonResponse;
    }
    // END-NOSCAN

}
