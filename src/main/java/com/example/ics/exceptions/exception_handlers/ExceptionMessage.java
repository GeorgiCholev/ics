package com.example.ics.exceptions.exception_handlers;

public class ExceptionMessage {

    private ExceptionMessage() {
    }

    public static final String NOT_STANDARD_URL = "URL is not compliant with global standards.";
    public static final String NOT_IMAGE_URL = "URL does not point to an image.";
    public static final String NOT_CATEGORIZABLE_IMAGE = "Image could not be categorized.";
    public static final String NOT_FOUND_IMAGE = "Image not found.";
    public static final String NOT_FOUND_PAGE = "Image gallery with specified arguments is empty.";
    public static final String NOT_ACCEPTED_REQUEST = "Request contains invalid parameters in URL.";
}
