package playwright;

public class Arguments {
    private Arguments() {}


    public static final String LANDSCAPE_IMG_ID = "d7b56133-68d4-4153-b4ce-c5f249d4f4be";
    public static final String LANDSCAPE_IMG_URL = "https://www.alleycat.org/wp-content/uploads/2019/03/FELV-cat.jpg";

    public static final String PORTRAIT_IMG_ID = "c2b44109-819d-4540-90df-11730f3decfb";
    public static final String PORTRAIT_IMG_URL = "https://images.unsplash.com/photo-1558788353-f76d92427f16?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8ZG9nJTIwcG9ydHJhaXR8ZW58MHx8MHx8fDA%3D&w=1000&q=80";

    public static final String BASE_ENDPOINT = "http://localhost:4200/";
    public static final String IMAGE_DETAIL_ENDPOINT = "http://localhost:4200/images/%s";
    public static final String IMAGE_GALLERY_ENDPOINT = "http://localhost:4200/images";
    public static final String IMAGE_GALLERY_ENDPOINT_WITH_PARAMS = "http://localhost:4200/images?%s";
    public static final String DATA_TEST_ID_LOCATOR = "data-test-id=%s";

    public static final String IMAGE_DETAIL_TITLE = "ics | Image Detail";
    public static final String IMAGE_GALLERY_TITLE = "ics | Gallery";
    public static final String CATEGORISATION_TITLE = "ics | Categorisation";


}
