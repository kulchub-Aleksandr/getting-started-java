package tests;

import core.TestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pages.RepositoryPage;
import pages.SearchPage;
import pages.SearchResultsPage;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;

public class SearchTests extends TestBase {
    private static final String EXISTING_QUERY = "qa.guru";
    private static final String EXISTING_REPOSITORY = "qa-guru/niffler";
    private static final String MISSING_QUERY = "qwertyuiop-nonexistent-987654321";

    private static SearchPage openSearch() {
        return open("/search", SearchPage.class);
    }

    @Test
    @DisplayName("Поиск существующего запроса возвращает релевантный репозиторий")
    void successfulSearchTest() {
        openSearch()
                .search(EXISTING_QUERY)
                .repositoryLink(EXISTING_REPOSITORY)
                .shouldBe(visible);
    }

    @Test
    @DisplayName("Поиск несуществующего запроса не возвращает репозиториев")
    void searchWithoutMatchesTest() {
        SearchResultsPage results = openSearch().search(MISSING_QUERY);

        results.emptyState().shouldBe(visible);
        results.results().shouldNot(exist);
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource({
            "selenide, selenide/selenide, selenide",
            "qa.guru, qa-guru/niffler, niffler"
    })
    @DisplayName("Из результатов поиска открывается найденный репозиторий")
    void openRepositoryFromSearchResultsTest(String query, String fullName, String shortName) {
        RepositoryPage repository = openSearch()
                .search(query)
                .openRepository(fullName);

        repository.name().shouldHave(text(shortName));
        repository.content().shouldBe(visible);
    }
}
