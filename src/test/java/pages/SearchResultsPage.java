package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class SearchResultsPage {
    private static final String RESULTS_LIST = "[data-testid='results-list']";
    private static final String EMPTY_STATE = "[data-testid='search-sub-header']";

    public SelenideElement results() {
        return $(RESULTS_LIST);
    }

    public SelenideElement emptyState() {
        return $(EMPTY_STATE);
    }

    public SelenideElement repositoryLink(String fullName) {
        return $$(RESULTS_LIST + " h3 a").findBy(text(fullName));
    }

    public RepositoryPage openRepository(String fullName) {
        repositoryLink(fullName).click();
        return new RepositoryPage();
    }
}
