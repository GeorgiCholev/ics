package com.example.ics.rest_assured;

public class TestArguments {

    private TestArguments() {}

//    PATHS
    public static final String BASE_URL = "http://localhost:8080";
    public static final String PATH = "/images";
    public static final String PATH_WITH_ID = PATH + "/{id}";


    public static final String ERROR_JSON_TEMPLATE_PATH = "src/test/resources/json-templates/error-received-template.json";
    public static final String IMAGE_JSON_TEMPLATE_PATH = "src/test/resources/json-templates/image-response-template.json";
    public static final String ALL_IMAGES_JSON_TEMPLATE_PATH = "src/test/resources/json-templates/all-images-template.json";


//    URLs
    public static final String VALID_URL =
            "https://images.unsplash.com/photo-1551884831-bbf3cdc6469e?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8ZnVubnklMjBob3JzZXxlbnwwfHwwfHx8MA%3D%3D&w=1000&q=80";
    public static final String NOT_CATEGORIZABLE_IMG_URL =
            "https://w0.peakpx.com/wallpaper/123/54/HD-wallpaper-scenery-lake-nature-sky-tree-water.jpg";
    public static final String NOT_IMG_URL = "https://github.com/";
    public static final String NOT_VALID_URL = "NotValidUrl";

//    IDs
    public static final String VALID_IMG_ID = "ae1ed32f-c48b-4fcb-b08d-8a9f1315570a";
    public static final String NOT_VALID_IMG_ID = "NotValidId";
}
