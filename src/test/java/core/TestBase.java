package core;

import com.codeborne.selenide.Configuration;
import config.TestConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public abstract class TestBase {
    @BeforeAll
    static void configureSelenide() {
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
    }
}
