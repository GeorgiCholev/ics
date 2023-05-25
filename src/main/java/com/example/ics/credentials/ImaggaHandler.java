package com.example.ics.credentials;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class ImaggaHandler {

    private static final String IMAGGA_ENDPOINT_URL_FORMAT = "https://api.imagga.com/v2/tags?image_url=%s&limit=%d";
    private final String encodedCredentials;

    public ImaggaHandler() {
        this.encodedCredentials = Base64.getEncoder()
                .encodeToString(
                        (System.getenv("IMAGGA_API_KEY") + ":" + System.getenv("IMAGGA_API_SECRET"))
                                .getBytes(StandardCharsets.UTF_8));
    }

    //    START-NOSCAN
    public String call(String address, int tagLimit) {
        String imaggaFinalUrl = constructImaggaFinalUrl(address, tagLimit);

        String jsonResponse;

        try {
            URL url = new URL(imaggaFinalUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization", "Basic " + this.encodedCredentials);

            if (connection.getResponseCode() >= 400) {
                return null;
            }

            BufferedReader connectionInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            jsonResponse = connectionInput.readLine();

            connectionInput.close();

        } catch (IOException e) {
            return null;
        }

        return jsonResponse;
    }
    private String constructImaggaFinalUrl(String address, int tagLimit) {
        return String.format(IMAGGA_ENDPOINT_URL_FORMAT, address, tagLimit);
    }

    //    END-NOSCAN
}
