package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class SearchPage {
    private final SelenideElement searchInput = $("[aria-label='Search GitHub']");

    public SearchResultsPage search(String query) {
        searchInput.setValue(query).pressEnter();
        return new SearchResultsPage();
    }
}
