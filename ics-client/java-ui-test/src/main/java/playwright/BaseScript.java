package playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class BaseScript {
    protected static Playwright playwright;
    protected static Browser browser;

    protected BrowserContext browserContext;
    protected Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
    }

    @BeforeEach
    void createContextAndPage() {
        this.browserContext = browser.newContext();
        this.page = this.browserContext.newPage();
    }

    @AfterEach
    void closeContext() {
        this.browserContext.close();
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }
}
