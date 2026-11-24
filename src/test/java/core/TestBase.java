package core;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.TestConfig;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public abstract class TestBase {
    private static final String ALLURE_LISTENER = "allure";

    @BeforeAll
    static void configureSelenide() {
        SelenideLogger.removeListener(ALLURE_LISTENER);
        SelenideLogger.addListener(ALLURE_LISTENER, new AllureSelenide()
                .screenshots(true)
                .savePageSource(true));
        Configuration.baseUrl = TestConfig.baseUrl();
        Configuration.browser = TestConfig.browser();
        Configuration.headless = TestConfig.headless();
        Configuration.browserSize = TestConfig.browserSize();
        Configuration.timeout = TestConfig.timeoutMillis();
        TestConfig.remote().ifPresent(value -> Configuration.remote = value);
    }

    @AfterAll
    static void closeBrowser() {
        closeWebDriver();
        SelenideLogger.removeListener(ALLURE_LISTENER);
    }
}
