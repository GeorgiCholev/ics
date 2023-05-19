package com.example.ics.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageAddressValidator implements ConstraintValidator<ValidImageAddress, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            URL url = new URL(value);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("HEAD");
            int responseCode = urlConnection.getResponseCode();

            if (responseCode < 200 || responseCode >= 400) {
                return false;
            }

            String contentType = urlConnection.getContentType();
            return contentType != null && contentType.startsWith("image/");
        } catch (IOException e) {
            return false;
        }
    }
}
