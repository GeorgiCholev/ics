package playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static playwright.Arguments.*;


public class CategorisationPageTest extends BaseScript {


    @BeforeEach
    void setEndpoint() {
        page.navigate(BASE_ENDPOINT);
    }

    @Test
    @DisplayName("Analyse navigation button is active and gallery is inactive")
    void testNavigationButtons_AnalyseIsDisabled_GalleryIsEnabled() {
        List<Locator> locators = page.locator(".active").all();
        assertEquals(1, locators.size());

        Locator locator = locators.get(0);
        assertEquals("Analyse", locator.textContent().trim());

    }

    @Test
    @DisplayName("Filling the input field with invalid data shows error pop-up")
    void testInvalidFormInput_ShowsExpectedErrorMessage() {
        page.fill("#url-field", "Invalid Input");
        assertTrue(page.isVisible(String.format(DATA_TEST_ID_LOCATOR, "invalid-url-error")));

        assertTrue(page.isDisabled("data-test-id=submit-btn"));
    }

    @Test
    @DisplayName("Not filling the input field shows error pop-up")
    void testEmptyFormInput_ShowsExpectedErrorMessage() {

        page.fill("#url-field", "X");
        page.fill("#url-field", "");

        assertTrue(page.isVisible(String.format(DATA_TEST_ID_LOCATOR, "required-field-error")));
        assertTrue(page.isDisabled(String.format(DATA_TEST_ID_LOCATOR, "submit-btn")));
    }

    @Test
    @DisplayName("Supplying not image Url shows error pop-up for 5 sec")
    void testNotImgUrl_ShowsExpectedErrorMessage() throws InterruptedException {

        page.fill("#url-field", "https://github.com/");
        assertFalse(page.isDisabled(String.format(DATA_TEST_ID_LOCATOR, "submit-btn")));
        page.click(String.format(DATA_TEST_ID_LOCATOR, "submit-btn"));
        page.waitForLoadState(LoadState.NETWORKIDLE);

        Locator locator = page.locator(String.format(DATA_TEST_ID_LOCATOR, "api-response-error"));
        assertTrue(locator.isVisible());
        assertEquals("URL does not point to an image.", locator.textContent());

        Thread.sleep(5_000);

        assertFalse(locator.isVisible());
    }

    @Test
    @DisplayName("Supplying image Url redirects to Image Detail page")
    void testImgUrl_redirectToImageDetailPage() {

        page.fill("#url-field", LANDSCAPE_IMG_URL);
        assertFalse(page.isDisabled(String.format(DATA_TEST_ID_LOCATOR, "submit-btn")));
        page.click("data-test-id=submit-btn");
        page.waitForLoadState(LoadState.NETWORKIDLE);

        assertTrue(page.isVisible(String.format(DATA_TEST_ID_LOCATOR, "image-detail-article")));
        assertEquals(IMAGE_DETAIL_TITLE, page.title());
    }
}
