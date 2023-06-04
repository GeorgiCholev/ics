package playwright;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.LoadState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static playwright.Arguments.*;

public class ImageDetailPageTest extends BaseScript {

    @Test
    @DisplayName("Navigation buttons aren't active")
    void testNavigationButtons_AnalyseIsDisabled_GalleryIsEnabled() {
        page.navigate(String.format(IMAGE_DETAIL_ENDPOINT, LANDSCAPE_IMG_ID));
        assertFalse(page.isVisible(".active"));
    }

    @Test
    @DisplayName("Visualized image is of path variable id")
    void testVisualizedImg_IsOfPathId() {
        page.navigate(String.format(IMAGE_DETAIL_ENDPOINT, LANDSCAPE_IMG_ID));
        page.waitForLoadState(LoadState.NETWORKIDLE);

        String imgUrl = page.getAttribute(".img-ctr > img", "src");
        assertEquals(LANDSCAPE_IMG_URL, imgUrl);
    }

    @ParameterizedTest
    @CsvSource({LANDSCAPE_IMG_ID + ", " + LANDSCAPE_IMG_URL + ", #landscape",
            PORTRAIT_IMG_ID + ", " + PORTRAIT_IMG_URL + ", #portrait"})
    @DisplayName("Article has an id portrait of landscape depending on image width and height")
    void testArticle_AddsCorrectId(String imageId, String imageUrl, String articleTagId) {

        page.navigate(String.format(IMAGE_DETAIL_ENDPOINT, imageId));
        page.waitForLoadState(LoadState.NETWORKIDLE);

        Locator locator = page.locator(articleTagId);
        assertTrue(locator.isVisible());

        String url = page.getAttribute(".img-ctr > img", "src");
        assertEquals(imageUrl, url);
    }

    @Test
    @DisplayName("Clicking a tag takes you to gallery with the tag as filter")
    void testClickingTag_TakesYouToGalleryWithTagAsFilter() {
        page.navigate(String.format(IMAGE_DETAIL_ENDPOINT, LANDSCAPE_IMG_ID));
        page.waitForLoadState(LoadState.NETWORKIDLE);

        List<Locator> locators = page.locator(".tag").all();

        assertEquals(5, locators.size());

        Locator first = locators.get(0);
        String tagLabel = first.textContent().trim().split("\\s+")[0];

        first.click();
        page.waitForLoadState(LoadState.NETWORKIDLE);

        assertEquals(page.title(), IMAGE_GALLERY_TITLE);

        assertEquals( String.format(IMAGE_GALLERY_ENDPOINT_WITH_PARAMS, "tag=" + tagLabel), page.url());
    }
}
