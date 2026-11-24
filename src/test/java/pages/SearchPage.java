package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class SearchPage {
    private final SelenideElement searchInput = $("[aria-label='Search GitHub']");

    @Step("Выполнить поиск")
    public SearchResultsPage search(String query) {
        searchInput.setValue(query).pressEnter();
        return new SearchResultsPage();
    }
}
