package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class RepositoryPage {
    private final SelenideElement name = $("[data-testid='repo-name-link']");
    private final SelenideElement content = $("[data-testid='directory-content']");

    public SelenideElement name() {
        return name;
    }

    public SelenideElement content() {
        return content;
    }
}
