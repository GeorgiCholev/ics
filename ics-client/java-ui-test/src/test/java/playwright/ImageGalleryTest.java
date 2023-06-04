package playwright;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.LoadState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static playwright.Arguments.*;

public class ImageGalleryTest extends BaseScript {

    @Test
    @DisplayName("Gallery navigation button is active and analyse is inactive")
    void testNavigationButtons_AnalyseIsDisabled_GalleryIsEnabled() {
        page.navigate(IMAGE_GALLERY_ENDPOINT);
        List<Locator> locators = page.locator(".active").all();
        assertEquals(1, locators.size());

        Locator locator = locators.get(0);
        assertEquals("Gallery", locator.textContent().trim());

    }

    @Test
    @DisplayName("Initial Gallery load has 12 items")
    void testGalleryCtrHas12Items() {
        page.navigate(IMAGE_GALLERY_ENDPOINT);
        page.waitForLoadState(LoadState.NETWORKIDLE);

        List<Locator> locators = page.locator(".gallery-item").all();
        assertEquals(12, locators.size());
    }

    @Test
    @DisplayName("Clicking image takes you to its detail page")
    void testClickImage_TakesToDetailPage() {
        page.navigate(IMAGE_GALLERY_ENDPOINT);
        page.waitForLoadState(LoadState.NETWORKIDLE);

        List<Locator> locators = page.locator(".item-ctr > a").all();
        Locator locator = locators.get(0);

        Locator imageLocator = locator.locator("img");
        String imageUrl = imageLocator.getAttribute("src");

        locator.click();
        page.waitForLoadState(LoadState.NETWORKIDLE);

        assertEquals(page.title(), IMAGE_DETAIL_TITLE);
        assertEquals(imageUrl, page.getAttribute(".img-ctr > img", "src"));
    }

    @Test
    @DisplayName("Reset all button removes selected tags")
    void testClickResetAllBtn_ResetsSelectedTags() {
        page.navigate(String.format(IMAGE_GALLERY_ENDPOINT_WITH_PARAMS, "tag=cat"));
        page.waitForLoadState(LoadState.NETWORKIDLE);

        page.locator(String.format(DATA_TEST_ID_LOCATOR, "reset-all"))
                .click();

        assertEquals(IMAGE_GALLERY_ENDPOINT, page.url());
    }

    @Test
    @DisplayName("Add button adds tags to next search and corresponding 'X' removes them")
    void testAddButtonAddsTags_XRemovesThem() {
        page.navigate(IMAGE_GALLERY_ENDPOINT);
        page.waitForLoadState(LoadState.NETWORKIDLE);

        Locator selectedTagsCtr = page.locator(String.format(DATA_TEST_ID_LOCATOR, "selected-tags-ctr"));
        assertFalse(selectedTagsCtr.isVisible());

        page.locator(String.format(DATA_TEST_ID_LOCATOR, "input-tag")).fill("cat");
        page.locator(String.format(DATA_TEST_ID_LOCATOR, "add")).click();

        assertTrue(selectedTagsCtr.isVisible());

        Locator selectedTag = page.locator(".selected-tag")
                .all()
                .get(0);

        String tagName = selectedTag
                .textContent()
                .trim()
                .split("\\s+")[0];

        assertEquals("cat", tagName);

        selectedTag.locator(".delete").click();

        assertFalse(selectedTagsCtr.isVisible());
    }

    @Test
    @DisplayName("Search button loads a gallery with selected tags")
    void testSearchBtn_LoadsGalleryWithSelectedTags() {
        page.navigate(IMAGE_GALLERY_ENDPOINT);
        page.waitForLoadState(LoadState.NETWORKIDLE);

        page.locator(String.format(DATA_TEST_ID_LOCATOR, "input-tag")).fill("cat");
        page.locator(String.format(DATA_TEST_ID_LOCATOR, "add")).click();

        page.locator(String.format(DATA_TEST_ID_LOCATOR, "search")).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);

        assertEquals(String.format(IMAGE_GALLERY_ENDPOINT_WITH_PARAMS, "tag=cat"), page.url());
    }
}
