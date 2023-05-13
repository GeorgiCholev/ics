package com.example.ics.utilities;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class ImaggaCredentials {

    private static final String IMAGGA_API_KEY = System.getenv("IMAGGA_API_KEY");
    private static final String IMAGGA_API_SECRET = System.getenv("IMAGGA_API_SECRET");

    private final String encodedCredentials;

    public ImaggaCredentials() {
        String credentialsToEncode = IMAGGA_API_KEY + ":" + IMAGGA_API_SECRET;
        this.encodedCredentials = Base64.getEncoder()
                .encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));
    }

    public String getEncodedCredentials() {
        return encodedCredentials;
    }
}
