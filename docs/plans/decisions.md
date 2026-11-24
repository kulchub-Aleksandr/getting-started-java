# Журнал решений (единый источник истины)

Обновляется агентом в каждой фазе. Если код расходится с этим файлом — это дефект, а не «новая норма».
Статусы: `открыт` → `решён` → `пересмотрен`.

## 1. Стилевые решения (S1–S12)

**Статус: подтверждены студентом 2026-11-24.** Все пункты — `решён`; изменения только через `пересмотрен` с записью причины.

| ID | Вопрос | Решение | Обоснование | Фаза |
|---|---|---|---|---|
| S1 | Пакеты: `tests/`, `pages/`, `config/` или package-by-feature | `tests/`, `pages/`, `core/`, `config/` (слои) | Предсказуемо для учебного курса; минимум вложенности; легко показать на слайдах | 0 |
| S2 | PageObject: Selenide `PageObject` + `@FindBy` / композиция `$`/`$$` / компоненты | Композиция `$`/`$$` внутри page-классов; `PageObject` только там, где нужен `open(Class)`; без `@FindBy` | Меньше магии PageFactory, локаторы видны рядом с методом, проще рефакторить | 0 |
| S3 | Ассерты: в page-методах или только в тестах | Только в тестах; page-методы возвращают элемент/страницу/данные | PageObject не знает про ожидания бизнес-результата; тест читается как сценарий | 0 |
| S4 | Fluent API: возвращать `this` / следующий page | Действия → `this`, навигация → следующий page | Читаемая цепочка шагов и явный переход между экранами | 0 |
| S5 | `@Step` на page-методах или отдельный `steps/` | `@Step` на page-методах; отдельный `steps/` не вводим | Один слой меньше; шаги Allure совпадают с действиями пользователя | 0 |
| S6 | Навигация: `open()` в тесте или `page.open()` | `open(Page.class)` в тесте, baseUrl из конфига | Тест сам задаёт стартовую точку, независимость и явность | 0 |
| S7 | Жизненный цикл браузера: класс / метод, переиспользование | Один браузер на класс (дефолт Selenide), `closeWebDriver()` в `@AfterAll`; без перезапуска на метод | Скорость; тесты не зависят от cookies/порядка; перезапуск на метод — только если появится загрязнение состояния | 0 |
| S8 | Тестовые данные: константы / билдеры / Faker / параметризация | Константы + `@ParameterizedTest`; без Faker/Lombok | Новых зависимостей не нужно; параметризация даёт реальное покрытие | 0 |
| S9 | Язык имён тестов и `@DisplayName` | Методы на английском, `@DisplayName` на русском | Технические имена в отчётах/логах, читаемость для аудитории курса | 0 |
| S10 | Allure-метаданные: обязательны или опциональны | Минимум обязателен: `@Feature`, `@Story`, `@Severity`; `@Owner` опционален | Отчёт группируется без раздувания аннотаций | 0 |
| S11 | Параллельный запуск JUnit | Выключен в фазах 1–4; включать после стабилизации и с per-class изоляцией | `github.com/search` + rate-limit/captcha и общий браузер делают параллель рискованной | 0 |
| S12 | Конфиг: `Configuration` / `selenide.properties` / env | System property → env → default через `config/TestConfig` + `Configuration` в `TestBase`; `selenide.properties` не используем | Один код локально и в CI; секреты не попадают в файл | 0 |

## 2. Зафиксированные версии

| Компонент | Версия | Источник/причина | Фаза |
|---|---|---|---|
| Java (локально) | Temurin 25.0.5+8 | `java -version` (факт) | 0 |
| Gradle | 9.6.0 | `gradle-wrapper.properties` | 0 |
| Selenide | 7.18.2 (Selenium 4.49.0) | `build.gradle`; [release notes](https://github.com/selenide/selenide/releases/tag/v7.18.2) | 0 |
| JUnit Jupiter | 6.1.3 | `build.gradle`; [Maven Central](https://repo1.maven.org/maven2/org/junit/jupiter/junit-jupiter/6.1.3/) | 0 |
| Allure Java | `allure-bom` + `allure-jupiter` + `allure-selenide` **3.1.1** | [Maven Central](https://repo1.maven.org/maven2/io/qameta/allure/allure-jupiter/maven-metadata.xml); Java 17+; JUnit 6 / Selenide 7.x | 3 |
| Allure CLI | npm `allure` **3.33.0** (`npx allure@3.33.0`), Node.js **≥ 22.18.0** (локально 24.15.0) | [registry.npmjs.org/allure](https://registry.npmjs.org/allure/latest), [Allure 3 README](https://github.com/allure-framework/allure3/blob/main/README.md); `engines.node` в npm-манифесте; проверено `npx allure@3.33.0 --version` → `3.33.0` | 3 |
| AspectJ Weaver | **1.9.27** (только test runtime, `-javaagent`) | [Maven Central](https://repo1.maven.org/maven2/org/aspectj/aspectjweaver/maven-metadata.xml); требуется для `@Step`/`@Attachment` | 3 |
| GitHub Actions runner | `ubuntu-latest` = Ubuntu 24.04 | [runner image](https://github.com/actions/runner-images/blob/main/images/ubuntu/Ubuntu2404-Readme.md); Chrome/ChromeDriver `157.0.8103.78`, Node.js `22.23.5` предустановлены; версии подтверждены логом run | 4 |
| `actions/checkout` | **v7** (v7.0.1) | [releases](https://github.com/actions/checkout/releases); текущий мажор | 4 |
| `actions/setup-java` | **v6** (v6.3.0), `distribution: temurin`, `java-version: '25'`, `cache: gradle` | [README](https://github.com/actions/setup-java); v1–v4 deprecated, v6 — текущий; Java 25 и Gradle-кэш подтверждены run | 4 |
| `actions/upload-artifact` | **v7** (v7.0.2) | [releases](https://github.com/actions/upload-artifact/releases) | 4 |
| `actions/upload-pages-artifact` | **v5** (v5.0.0) | [README](https://github.com/actions/upload-pages-artifact) | 4 |
| `actions/deploy-pages` | **v5** (v5.0.1) | [README](https://github.com/actions/deploy-pages) | 4 |
| Allure CLI в CI | Node.js `22.23.5` (runner) + `npx --yes allure@3.33.0` | та же версия, что локально; Node ≥ 22.18 | 4 |
| Jenkins | `allure-jenkins-plugin` **3.0.0** (требует Jenkins ≥ 2.541.3) | [plugins.jenkins.io](https://plugins.jenkins.io/allure-jenkins-plugin/) | 6 |

### 2.1 Совместимость (проверено 2026-11-24)

| Вопрос | Вердикт | Источник | Влияние на план |
|---|---|---|---|
| `allure-junit5` × JUnit 6.1.3 | **Да, но артефакт заменён**: `allure-junit5` в 3.x не публикуется, использовать `allure-jupiter` 3.1.1 (валидирован против JUnit 6.1.0 platform API); **прогон на JUnit 6.1.3 зелёный** (4/4, 2026-11-24) | [allure-jupiter README](https://github.com/allure-framework/allure-java/blob/main/allure-jupiter/README.md), `build/test-results/test/TEST-tests.SearchTests.xml` | Фаза 3: подключать `allure-jupiter`, не `allure-junit5`; результаты пишутся, тесты видны |
| `allure-selenide` × Allure 3 | **Да**: `allure-selenide` 3.1.1, targets Selenide 7.x (сборка валидирована на 7.17.0; у нас 7.18.2 — прогон зелёный) | [allure-selenide README](https://github.com/allure-framework/allure-java/blob/main/allure-selenide/README.md) | Фаза 3: `AllureSelenide` через `SelenideLogger` в `TestBase`; вложения только на `FAIL` |
| Allure Report 3: установка CLI | **Да**: npm-пакет `allure` 3.33.0, `npm i -g allure` или `npx allure@3.33.0`; результаты — каталог с именем `allure-results` на любом уровне вложенности | [Allure 3 README](https://github.com/allure-framework/allure3/blob/main/README.md), [npm](https://www.npmjs.com/package/allure) | Фаза 3: `npx allure@3.33.0 generate build/allure-results`; `npx allure@3.33.0 open build/allure-report` |
| Allure 3 `generate`: флаг `-o/--output` | **Да (проверено запуском)**: `--output,-o` присутствует; default `allure-report`; также `--clean`, `--open`, `--port`, `--host` | `npx allure@3.33.0 generate --help` (2026-11-24) | Фаза 3: используем `allurerc.mjs` с `output: build/allure-report` + `--clean` |
| Allure 3 `generate`: default report | **Awesome**, `index.html` кладётся в корень output-каталога (не в `awesome/`, несмотря на `href` в `summary.json`) | фактический прогон: `build/allure-report/index.html`; `summary.json` → `"plugin":"Awesome"` | Фаза 3: открывать `build/allure-report`, не вложенный `awesome/` |
| `@Step` × Allure Java 3.1.1 | **Требует AspectJ weaver**: аннотации `@Step`/`@Attachment` не работают без `-javaagent`; с `aspectjweaver` 1.9.27 шаги появились в result JSON | [allure-java-commons README](https://github.com/allure-framework/allure-java/blob/main/allure-java-commons/README.md), [Maven Central](https://repo1.maven.org/maven2/org/aspectj/aspectjweaver/maven-metadata.xml) | Фаза 3: `testRuntimeOnly` + конфигурация `aspectj` + `-javaagent` в `tasks.withType(Test)` |
| Каталог результатов Allure Java | **По умолчанию `allure-results` в рабочем каталоге**, не `build/allure-results` (последнее — дефолт Gradle-плагина Allure, который мы не применяем) | прогон без явной настройки + README `allure-jupiter` («usually `build/allure-results` for Gradle» относится к плагину) | Фаза 3: явно `systemProperty 'allure.results.directory'` → `build/allure-results` |
| Selenide 7.18.2 × Java 25 | **Да (эмпирически)**: baseline-прогон зелёный на Java 25.0.5; официального заявления про Java 25 нет | `build/test-results/test/TEST-SearchTests.xml`; [Selenide 7.18.2](https://github.com/selenide/selenide/releases/tag/v7.18.2) | Ограничений не вносим; при проблемах в CI — отдельное решение |
| Jenkins Allure plugin × Allure 3 | **Да**: plugin 3.0.0, «Recommended Allure 3» по умолчанию, встроенный Node.js | [plugins.jenkins.io/allure-jenkins-plugin](https://plugins.jenkins.io/allure-jenkins-plugin/) | Фаза 6: использовать plugin 3.0.0; fallback CLI + `publishHTML` не обязателен |
| GHA: Chrome/драйвер на `ubuntu-latest` | **Да, ставить не нужно**: образ 24.04 содержит Google Chrome `157.0.8103.78` + ChromeDriver `157.0.8103.78`; run подтвердил обе версии | [Ubuntu 24.04](https://github.com/actions/runner-images/blob/main/images/ubuntu/Ubuntu2404-Readme.md), лог run [45379934914](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45379934914) | Фаза 4: шага установки браузера нет; Selenium Manager использует предустановленный Chrome |
| GHA Pages: `upload-pages-artifact@v5` + `deploy-pages@v5` | **Да**: отдельный deploy job, `pages: write` + `id-token: write`, environment `github-pages`; Pages source — GitHub Actions (`build_type=workflow`) | [deploy-pages README](https://github.com/actions/deploy-pages), [upload-pages-artifact](https://github.com/actions/upload-pages-artifact) | Фаза 4: отчёт опубликован на https://kulchub-aleksandr.github.io/getting-started-java/ |
| Selenium Manager vs setup-action | **Selenium Manager** встроен в Selenium 4.6+/4.49; сам находит/скачивает драйвер и браузер; `setup-chrome@v2` ставит конкретную версию Chrome + совместимый ChromeDriver | [Selenium Manager](https://www.selenium.dev/documentation/selenium_manager/), [setup-chrome](https://github.com/browser-actions/setup-chrome) | Фаза 4: по умолчанию Selenium Manager + предустановленный Chrome; setup-action — если нужен пин |
| Selenide 7.18.2: `PageObject` / `open(Class)` / `@Url` | **Отсутствуют**: в `selenide-core-7.18.2.jar` нет `com.codeborne.selenide.PageObject` и аннотации `@Url`; у `Selenide.open` нет перегрузки `open(Class)` — только `open(String, Class)` | `jar tf` + `javap` по артефакту 7.18.2 (проверено 2026-11-24) | S2/S6 адаптированы: обычные page-классы + `open("/search", SearchPage.class)`; `@FindBy` не используется |

### 2.2 Адаптации решений по факту API (фаза 2)

| Решение | Как реализовано | Причина |
|---|---|---|
| S2 | Page-классы без наследования `PageObject`, локаторы через `$`/`$$` в полях/методах | `PageObject` удалён в Selenide 7.x; `@FindBy` не нужен |
| S5 | `@Step` пока не добавлен | Allure в фазе 2 не подключён (запрет фазы); аннотации — в фазе 3 |
| S6 | `open("/search", SearchPage.class)` в тесте; `baseUrl` из конфига | Нет `open(Class)`/`@Url`; перегрузка `open(String, Class)` сохраняет типизированный возврат |
| S8 | Константы для одиночных значений + `@ParameterizedTest`/`@CsvSource` для табличных кейсов | Новых зависимостей нет |
| S10 | Allure-аннотации отложены | Нет зависимости Allure в фазе 2 |

## 3. Целевая структура файлов

```text
src/test/java/
  config/TestConfig.java          # фаза 1: system property -> env -> default
  core/TestBase.java              # фаза 1: Configuration в @BeforeAll, closeWebDriver в @AfterAll
  pages/SearchPage.java           # композиция $/$$; действие search -> SearchResultsPage
  pages/SearchResultsPage.java    # результаты: список, пустое состояние, переход в репозиторий
  pages/RepositoryPage.java       # ключевые элементы страницы репозитория
  tests/SearchTests.java          # фаза 1: перенесён из default package, наследует TestBase
src/test/resources/
  logback-test.xml                # опционально, чтобы убрать SLF4J NOP (согласовать)
.github/workflows/
  tests.yml                       # фаза 4
.github/scripts/
  notify-telegram.sh              # фаза 5: шаблон сообщения + отправка (переиспользуется в Jenkins)
Jenkinsfile                       # фаза 6
allurerc.mjs                      # фаза 3 (output/plugins), если не хватит CLI-флагов
```

### 3.1 Конфигурация тестов (фаза 1)

Фактические классы: `config.TestConfig` (разрешение значений), `core.TestBase` (abstract, `@BeforeAll` → `Configuration`, `@AfterAll` → `closeWebDriver()`), `tests.SearchTests extends TestBase`.

Приоритет для каждого ключа: **system property → env → default**. Канонический `-D` имеет приоритет над Selenide-алиасом. `build.gradle` явно перечисляет только эти ключи в `tasks.withType(Test)`; `System.getProperties()` целиком не копируется.

| Ключ | `-D` | Env | Default |
|---|---|---|---|
| `baseUrl` | `-DbaseUrl` (алиас `-Dselenide.baseUrl`) | `BASE_URL` | `https://github.com` |
| `browser` | `-Dbrowser` (алиас `-Dselenide.browser`) | `BROWSER` | `chrome` |
| `headless` | `-Dheadless` (алиас `-Dselenide.headless`) | `HEADLESS` | `false` |
| `browserSize` | `-DbrowserSize` (алиас `-Dselenide.browserSize`) | `BROWSER_SIZE` | `1920x1080` |
| `timeout` | `-Dtimeout=10s` / `-Dtimeout=500ms`; нативный `-Dselenide.timeout` — миллисекунды | `TIMEOUT` (`10s`) | `10s` (10000 ms) |
| `remote` | `-Dremote` (алиас `-Dselenide.remote`) | `REMOTE` | пусто (локальный браузер) |

`timeout`: значение без единицы трактуется как секунды; `ms` — миллисекунды. Порядок для `timeout`: `-Dtimeout` → `-Dselenide.timeout` (ms) → `TIMEOUT` → 10000.

### 3.2 Page object слой (фаза 2)

Стиль: S2 — композиция `$`/`$$`, без `PageObject`/`@FindBy`; S3 — page-методы возвращают `SelenideElement`/next page, ассерты только в тестах; S4 — навигация возвращает следующий page; S6 — старт в тесте через `open("/search", SearchPage.class)`.

| Класс | URL | Ключевые методы | Локаторы (инкапсулированы) |
|---|---|---|---|
| `pages.SearchPage` | `/search` | `search(query)` → `SearchResultsPage` | `[aria-label='Search GitHub']` |
| `pages.SearchResultsPage` | `/search?q=...` | `results()`, `emptyState()`, `repositoryLink(fullName)`, `openRepository(fullName)` → `RepositoryPage` | `[data-testid='results-list']`, `[data-testid='results-list'] h3 a`, `[data-testid='search-sub-header']` |
| `pages.RepositoryPage` | `/owner/repo` | `name()`, `content()` | `[data-testid='repo-name-link']`, `[data-testid='directory-content']` |

Стратегия локаторов: только `data-testid`, `aria-label` и текст ссылки/репозитория. CSS-классы вида `Result__Of4h7n`, `header__ZqTG4W` не используются (хешируются при сборке). Текст результата (`qa-guru/niffler`, `selenide/selenide`) стабилен относительно запроса и найденного репозитория.

Тесты (`tests.SearchTests`, 3 метода / 4 выполнения):

| Тест | Что проверяет |
|---|---|
| `successfulSearchTest` | Поиск `qa.guru` → в выдаче есть ссылка на `qa-guru/niffler` |
| `searchWithoutMatchesTest` | Поиск несуществующего запроса → нет `results-list`, виден `search-sub-header` (структурный признак пустой выдачи, без привязки к формулировке) |
| `openRepositoryFromSearchResultsTest` (параметризован: `selenide` и `qa.guru`) | Переход по найденной ссылке → `repo-name-link` содержит имя репозитория, `directory-content` видим |

`@Step` и Allure-аннотации (`@Feature`, `@Story`, `@Severity`) добавляются в фазе 3 вместе с зависимостью Allure.

### 3.3 Allure 3 (фаза 3)

**Зависимости** (`build.gradle`): `platform("io.qameta.allure:allure-bom:3.1.1")`, `io.qameta.allure:allure-java-commons` (аннотации `@Feature`/`@Story`/`@Severity`/`@Step`), `io.qameta.allure:allure-jupiter`, `io.qameta.allure:allure-selenide`; `org.aspectj:aspectjweaver:1.9.27` (+ отдельная конфигурация `aspectj` для `-javaagent`). Последний нужен только для `@Step`; без него шаги Selenide пишутся, а `@Step` игнорируется (проверено).

**IDE**: если `@Feature`/`@Story`/`@Severity`/`@Step` подсвечены красным, а `./gradlew test` зелёный — это устаревшая Gradle-модель IDE, а не classpath. Лечится переимпортом Gradle-проекта (IDEA: «Reload Gradle Project», VS Code: «Java: Reload Projects»), не правкой импортов. `allure-java-commons` объявлен явно, чтобы аннотации были прямой зависимостью.

**Результаты**: `allure.results.directory` = `build/allure-results` (system property в `tasks.withType(Test)`). Это не дефолт библиотеки: без настройки Allure Java пишет в `./allure-results` (`AllureLifecycle#getDefaultWriter`, default `"allure-results"`). Каталог `build/` уже в `.gitignore`.

**Отчёт**: конфиг `allurerc.mjs` → `name: "getting-started-java"`, `output: "build/allure-report"`. Allure 3.33.0 генерирует Awesome-отчёт, `index.html` лежит в корне `build/allure-report`; каталог тоже под `build/`.

| Действие | Команда | Проверено |
|---|---|---|
| Прогон | `./gradlew clean test -Dselenide.headless=true` (PowerShell: `.\gradlew.bat clean test "-Dselenide.headless=true"`) | да, 4/4 passed |
| Генерация | `npx allure@3.33.0 generate build/allure-results --clean` | да, `summary.json`: total 4, passed 4 |
| Просмотр | `npx allure@3.33.0 open build/allure-report` | да, HTTP 200, `<title>getting-started-java</title>` |
| Альтернатива | `npx allure@3.33.0 generate ... --open` (флаг есть в `--help`) | флаг подтверждён, в паре не использовался |

`npx allure@3.33.0` скачивает CLI при первом запуске (нужен Node.js ≥ 22.18.0); версия пинуется, глобальная установка не требуется.

**Selenide → Allure**: `AllureSelenide` регистрируется в `TestBase@BeforeAll` (перед действиями) и снимается в `@AfterAll` после `closeWebDriver()`. `screenshots(true)`, `savePageSource(true)`; вложения пишутся только при `EventStatus.FAIL` (код `AllureSelenide#afterEvent`), не на каждый шаг. Дублирования нет: логгер один, имя `allure`, перед добавлением снимается старый.

**S5 `@Step`**: добавлены `SearchPage#search` и `SearchResultsPage#openRepository` (без placeholders — не требуется `-parameters`). В result JSON шаги видны: `$("open") ...` → `Выполнить поиск` → `Открыть репозиторий из результатов поиска` → Selenide-ассерты.

**S10**: добавлены `@Feature("Поиск на GitHub")` на класс, `@Story` + `@Severity` на каждый тест (CRITICAL / MINOR / NORMAL). `@Epic`/`@Owner` не добавлены: S10 требует минимум `@Feature`/`@Story`/`@Severity`, а без реального значения эти метки были бы ритуалом.

**Контролируемая поломка** (2026-11-24): `EXISTING_REPOSITORY` временно заменён на `qa-guru/nonexistent-987654321` → `successfulSearchTest` failed; в отчёте у упавшего шага `Screenshot` (`image/png`, 150 462 B) и `Page source` (`text/html`, 205 109 B); `summary.json` → `total 4, passed 3, failed 1`, `status: failed`. После отката: 4/4 passed, `status: passed`, вложений нет.

### 3.4 GitHub Actions (фаза 4)

Файл: `.github/workflows/tests.yml`. Триггеры `push`, `pull_request`, `workflow_dispatch`; `concurrency` по `${{ github.workflow }}-${{ github.ref }}` с `cancel-in-progress: true`; job `test` — `timeout-minutes: 30`.

| Блок | Реализация |
|---|---|
| Права | workflow: `contents: read`; deploy job: `pages: write`, `id-token: write` (без `contents: write`, gh-pages-ветка не используется) |
| JDK | `actions/setup-java@v6`, Temurin 25, `cache: gradle` (кэш зависимостей + wrapper) |
| Браузер/драйвер | **Вариант A**: Selenium Manager + предустановленный в runner Chrome; отдельного шага установки нет, шаг `Browser versions` только логирует версии |
| Прогон | `./gradlew clean test -Dselenide.headless=true` — та же команда и тот же ключ, что локально; `HEADLESS`/`BROWSER` env не задаются, локальный дефолт остаётся headed |
| Результаты | `actions/upload-artifact@v7`, имя **`allure-results`**, `if: always()`, retention 14 дней |
| Отчёт | `npx --yes allure@3.33.0 generate build/allure-results --clean` (та же версия CLI, что локально) |
| Публикация | `upload-pages-artifact@v5` + отдельный job `deploy` (`deploy-pages@v5`), environment `github-pages`; только для `master` при `push`/`workflow_dispatch`, PR и форки отчёт не публикуют |
| Summary | `$GITHUB_STEP_SUMMARY`: статус шага тестов, total/passed/failed/broken/skipped из `build/allure-report/summary.json`, ссылка на отчёт |

**Обоснование варианта A:** runner уже содержит согласованные Chrome и ChromeDriver, поэтому Selenium Manager не скачивает драйвер и не добавляет шаг/версию; локально и в CI используется один и тот же механизм Selenide. Риск — обновление Chrome в образе; при поломке мажорного драйвера fallback — `browser-actions/setup-chrome@v2` с пином версии (пока не нужен).

**Проверено 2026-11-24:**
- push-run [45379157672](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45379157672) — зелёный, отчёт опубликован;
- workflow_dispatch [45379628682](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45379628682) — зелёный, кэш Gradle hit, test job 52 с (первый 57 с);
- push-run [45379934914](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45379934914) — зелёный, test job 30 с, deploy 9 с; артефакт `allure-results` 4 550 B скачан и содержит 4 result JSON; отчёт доступен без авторизации (HTTP 200, `<title>getting-started-java</title>`).
- Контролируемое падение [45384436473](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45384436473): `EXISTING_REPOSITORY` временно заменён на `qa-guru/nonexistent-987654321` → `Run tests` failed (`ElementNotFound`/`NoSuchElementException` на `SearchTests.java:39`, 4 tests, 1 failed), но `Upload allure-results` (292 109 B; 4 result JSON + PNG-скриншот + HTML page source), `Generate Allure report`, `Upload Pages artifact`, `Job summary` и `deploy` (10 с) выполнились; опубликованный `summary.json` → `total 4, passed 3, failed 1`, `status: failed`. После отката — снова 4/4 и `status: passed`.

**Для фазы 5 переиспользуется:** имя артефакта `allure-results`, URL отчёта `https://kulchub-aleksandr.github.io/getting-started-java/`, job `test` и его conclusion (`success`/`failure`) — источник статуса для Telegram.

### 3.5 Telegram (фаза 5)

**Способ: `curl` к Bot API, без сторонних action.** Обоснование: нет зависимости от чужого репозитория и его мажорных обновлений; полный контроль над текстом, экранированием, ретраями и пропуском без секретов; тот же bash-скрипт переиспользуется в Jenkins (`withCredentials`), меняется только способ доставки credentials. Готовый action (`appleboy/telegram-action` и подобные) отклонён: добавляет сторонний код и не даёт переиспользовать шаблон в Jenkins.

Файлы: `.github/scripts/notify-telegram.sh` (шаблон сообщения + отправка), шаг `Notify Telegram` в job `test` с `if: always() && !cancelled()`.

| Параметр | Реализация |
|---|---|
| Триггер | один итоговый шаг в конце job `test`; `success` и `failure` отправляются, `cancelled` → `::notice::` и skip |
| Статус | `steps.tests.outcome`; пустой/`skipped` (тесты не запускались) трактуется как `failure` |
| Ветка / коммит | `github.head_ref || github.ref_name`; имя коммита — `github.event.head_commit.message` (push) или `github.event.pull_request.title` (PR), первая строка, ≤ 120 символов; если имени нет (`workflow_dispatch`) — короткий `github.sha` |
| Формат | `parse_mode=HTML`; экранируются только `&`, `<`, `>`; MarkdownV2 не используется, поэтому `_`, `.`, `[`, `]`, `(`, `)` в именах тестов безопасны |
| Упавшие тесты | `build/test-results/test/*.xml`: `<testcase>` + `<failure|error>`; XML-сущности раскрываются, затем HTML-экранируются; до 10 имён, имя ≤ 160 символов, остаток — «… and N more» |
| Ссылки | run, Allure Pages (если `REPORT_PUBLISHED=true`) или артефакты run; `disable_web_page_preview=true` |
| Длина | ограничена 10 именами; лимит Bot API 4096 символов не достигается |
| Секреты | `TELEGRAM_BOT_TOKEN`, `TELEGRAM_CHAT_ID` — только GitHub Secrets, передаются `env` только в шаг уведомления; GitHub маскирует их как `***` |
| Без секретов | пустые значения → `::notice::Telegram credentials are not configured, notification skipped`, `exit 0` |
| Ошибки API | 3 попытки с паузой 2 и 4 с; затем `::warning::` с HTTP-кодом и `description` из ответа, `exit 0`; токен и URL не логируются (`curl --silent`, без `set -x`) |
| Локальная проверка | `DRY_RUN=1` печатает готовый HTML в stdout без сети и без секретов |

Шаблон сообщения (пример для упавшего прогона):

```text
<b>UI tests: FAILURE</b>

Branch: <code>master</code>
Commit: Add phase 5 Telegram notification
Run: <a href="https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45396414475">run details</a>
Allure report: <a href="https://kulchub-aleksandr.github.io/getting-started-java/">open report</a>
Tests: 1 failed of 4

1. <b>SearchTests</b> — Поиск существующего запроса возвращает релевантный репозиторий
```

Проверено 2026-11-24:

| Прогон | Run | Результат | Уведомление |
|---|---|---|---|
| Зелёный (workflow + скрипт) | [45395989599](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45395989599) | success, 4/4 | `Telegram notification sent (success)` |
| Контролируемое падение (`EXISTING_REPOSITORY` → несуществующий) | [45396414475](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45396414475) | failure, 4 tests / 1 failed | `Telegram notification sent (failure)` |
| Откат теста | [45397540787](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45397540787) | success, 4/4, Pages `status: passed` | `Telegram notification sent (success)` |

Логи success- и failure-прогонов: `token_like` = 0, `api.telegram.org` = 0, `chat_id=` = 0; секреты в env-блоке — `***`.

**Переиспользование в Jenkins (фаза 6)** — тот же файл и шаблон, другая доставка credentials:

```groovy
def commitName = sh(returnStdout: true, script: 'git log -1 --pretty=%s').trim()
withCredentials([
  string(credentialsId: 'telegram-bot-token', variable: 'TELEGRAM_BOT_TOKEN'),
  string(credentialsId: 'telegram-chat-id', variable: 'TELEGRAM_CHAT_ID')
]) {
  withEnv([
    "STATUS=${currentBuild.currentResult == 'SUCCESS' ? 'success' : 'failure'}",
    "BRANCH=${env.BRANCH_NAME ?: 'master'}",
    "COMMIT_SHA=${env.GIT_COMMIT}",
    "COMMIT_NAME=${commitName}",
    "RUN_URL=${env.BUILD_URL}",
    "REPORT_URL=${env.BUILD_URL}allure/",
    "REPORT_PUBLISHED=true"
  ]) {
    sh 'bash .github/scripts/notify-telegram.sh'
  }
}
```

### 3.6 Jenkins (фаза 6)

**Статус: в работе; сборки #1–#4 `SUCCESS`; `Jenkinsfile` в SCM (`e6a0f5e`), success-сценарий с JUnit XML/Allure проверен; Telegram заблокирован областью credentials; assertion/compile/missing-results ещё не завершены.**

**Проверка предусловий 2026-11-24 (read-only, с Jenkins API token):** `https://jenkins.qa.guru/` отвечает `200`, `X-Jenkins: 2.581`; `/computer/api/json` доступен: `java-jdk21-jenkins-agent-1..5` online, labels `java,java-jdk21,<agent>,docker`; warm-agent без `docker`. Job `alekskulch-getting-started-java` существует (анонимно `404`, с токеном `200`); на момент проверки — inline Pipeline, build #2 `SUCCESS` (позже переключён на SCM). `/pluginManager/api/json` → `403` (нет прав администратора), поэтому версии плагинов через API не читаются; `/me` → `alekskulch`, authorities `/students,authenticated`. Label `docker` **не доказывает** наличие `docker.sock`. Telegram credentials в user store уже созданы: `telegram-bot-token`, `telegram-chat-id` (значения не читались).

Доступ: https://jenkins.qa.guru/ (анонимно read-only; для создания job и запуска нужен вход студента). Jenkins использует **Keycloak SSO** (`/login` → `/realms/qaguru/...`); HTTP Basic с паролем Keycloak отклоняется (`401`), поэтому для API нужен **Jenkins API token** (username + token), а не пароль. Токен и пароль не попадают в репозиторий/чат. Агенты с меткой `java-jdk21` (`java-jdk21-jenkins-agent-1..5`), JDK 21; warm-агент `java-jdk21-jenkins-warm-agent-1` без Docker. Плагин Allure Jenkins **3.0.0** поддерживает Allure 3, но по умолчанию `allureVersion` = 2, поэтому версия задаётся явно.

**Архитектура браузера — общий Selenoid (вместо Docker sidecar).** `https://selenoid.qa.guru/wd/hub`, basic auth, Chrome 148/157/158, 25 слотов. Причина: на агентах нет `docker.sock`; общий Selenoid — штатный ресурс Jenkins (им уже пользуется `autotests-ai-multistack-app`). Docker sidecar отклонён: требует DinD/`docker.sock`, которого нет. Локальный Chrome на агенте отклонён: не воспроизводит целевую среду и требует установки браузера на все агенты.

| Блок | Реализация |
|---|---|
| `Jenkinsfile` | Declarative, `agent { label 'java-jdk21' }`, `skipDefaultCheckout`, stages Checkout → Test → Report → Publish, публикация/уведомление в `post { always }` |
| Параметры | `disableConcurrentBuilds()`, `timeout 30 MINUTES`, `buildDiscarder(20)`, `timestamps()` |
| Браузер | `withCredentials([string(credentialsId: 'selenoid-guest-webdriver-url', variable: 'REMOTE')])`; URL не печатается; `TestConfig` читает env `REMOTE`, `TestBase` ставит `Configuration.remote` |
| Прогон | `./gradlew --no-daemon clean test -Dselenide.headless=true` в `catchError(buildResult: 'FAILURE', stageResult: 'FAILURE')`, чтобы отчёт строился и при падении тестов |
| Allure | `allure(allureVersion: '3', includeProperties: false, results: [[path: 'build/allure-results']], configPath: 'allurerc.mjs', report: 'build/allure-report', reportBuildPolicy: 'ALWAYS', resultPolicy: 'LEAVE_AS_IS')`; плагин вызывает CLI с `-o build/allure-report --config allurerc.mjs` (проверено по исходникам `ReportBuilder`), `index.html` — в корне `report` |
| Артефакты | Всегда архивируются `build/allure-results/**`, `build/test-results/**`, `build/reports/**`, `build/allure-report/**`; ссылка на отчёт `${BUILD_URL}allure/`; описание сборки — ссылка на отчёт |
| JUnit | `junit testResults: 'build/test-results/test/*.xml', allowEmptyResults: true` в `post { always }`; отсутствие XML — явная ошибка и `FAILURE`, а не зелёная сборка |
| Уведомление | `.github/scripts/notify-telegram.sh` (тот же, что в GHA) в `post { always }` по фактическому статусу; credentials `telegram-bot-token`, `telegram-chat-id`; `REPORT_PUBLISHED` — по наличию `build/allure-report/index.html`; без credentials — предупреждение и skip, сборка не падает |
| Секреты | Selenoid URL и Telegram token/chat id — только Jenkins credentials; значений в Jenkinsfile нет; `withCredentials` маскирует их в консоли; `set -x` в скрипте уведомления отсутствует |

**Расхождение среды:** агенты Jenkins — JDK 21, GHA — Temurin 25. Проект не задаёт toolchain; Gradle 9.6 требует Java 17+ для запуска, JDK 21 удовлетворяет; прогон на агенте выполнен успешно (`BUILD SUCCESSFUL`), версии проекта не меняются. Риск закрыт.

**Проверка сборки #1 (2026-11-24):** job `alekskulch-getting-started-java` (временный inline probe, не SCM), [build #1](https://jenkins.qa.guru/job/alekskulch-getting-started-java/1/) — `SUCCESS`, 4/4 passed, `BUILD SUCCESSFUL in 52s`; агент `java-jdk21-jenkins-agent-5`; `REMOTE` замаскирован, в консоли нет `selenoid.qa.guru`/`user:`; Allure CLI 3.13.0, отчёт `/job/alekskulch-getting-started-java/1/allure/` (HTTP 200 с авторизацией), `summary.json`: total 4, passed 4. На агентах нет локального Chrome/chromedriver (probe: `NO_LOCAL_CHROME`, `NO_LOCAL_DRIVER`), тесты шли через `Configuration.remote`. Telegram пропущен: `CredentialNotFoundException`; credentials не созданы. Remote `master` не содержит `Jenkinsfile` (404), поэтому job пока inline; после push переключить на SCM.

**Проверка сборки #2 (2026-11-24, повторно с токеном):** [build #2](https://jenkins.qa.guru/job/alekskulch-getting-started-java/2/) — `SUCCESS`, `BUILD SUCCESSFUL in 32s`; агент `java-jdk21-jenkins-agent-5`, workspace `/home/jenkins/agent/workspace/alekskulch-getting-started-java`; checkout `master` @ `837cce5`; `REMOTE` замаскирован; Allure CLI `3.13.0` из installation `Allure 3`, отчёт `/2/allure/` HTTP 200, `summary.json` → `total 4, passed 4, status passed`; артефакты: `allure-report.zip` + 4 result JSON + `environment.properties`/`executor.json`. **JUnit XML не публикуется** (в pipeline нет шага `junit`; историческая сборка до исправления), `build/test-results/**` и `build/reports/**` не архивируются. Telegram снова skip (`CredentialNotFoundException`; credentials созданы после #2). Job остаётся inline; `Jenkinsfile` в `master` отсутствует.

**Проверка сборки #3 (2026-11-24, актуальный `Jenkinsfile` через inline probe):** [build #3](https://jenkins.qa.guru/job/alekskulch-getting-started-java/3/) — `SUCCESS`, `BUILD SUCCESSFUL in 32s`; JUnit step `Recording test results`, `testReport/api/json` → `passCount 4`, `failCount 0`, `skipCount 0`; 79 артефактов, включая `build/test-results/test/TEST-tests.SearchTests.xml`, `build/reports/tests/test/**`, `build/allure-report/index.html`, `allure-report.zip`; Allure `summary.json` → `total 4, passed 4, status passed`; `/3/allure/` HTTP 200. Telegram снова skip (`CredentialNotFoundException`): credentials `telegram-bot-token`/`telegram-chat-id` существуют в **user store** `alekskulch` (`Secret text`), но pipeline их не видит; system store `/credentials/store/system/...` → `404` (нет прав администратора). Selenoid system credential в том же pipeline работает, поэтому причина — область/доступ credentials, а не синтаксис `withCredentials`. В консоли #3: Jenkins API token, `api.telegram.org`, `chat_id=`, `selenoid.qa.guru`, `://user@` — 0 вхождений.

**Проверка сборки #4 (2026-11-24, Pipeline from SCM после push):** коммит `e6a0f5e` запушен в `origin/master`; job переключён с inline на `CpsScmFlowDefinition` (`https://github.com/kulchub-Aleksandr/getting-started-java.git`, `*/master`, `Jenkinsfile`, lightweight). [build #4](https://jenkins.qa.guru/job/alekskulch-getting-started-java/4/) — `SUCCESS`, checkout `e6a0f5e3918a1b97a35a87ade75b100059f09690`, `BUILD SUCCESSFUL in 33s`; JUnit `pass=4`, `fail=0`; 79 артефактов (`TEST-tests.SearchTests.xml`, `build/reports/tests/test/**`, `build/allure-report/index.html`, `allure-report.zip`); Allure `total 4, passed 4, status passed`; `/4/allure/` HTTP 200. Telegram снова skip (`CredentialNotFoundException`) — user-store credentials не видны pipeline; в консоли нет токена, `api.telegram.org`, `chat_id=`, Selenoid URL и `://user@`. SCM-переход подтверждён.

**Контролируемое падение (2026-11-24):** временный job с тестом `IntentionalFailureTest` — `FAILURE`, 5 tests / 1 failed, Allure `status=failed` (4 passed, 1 failed), отчёт HTTP 200; Telegram снова skip (`CredentialNotFoundException`); тест удалён из workspace, job удалён после проверки. Это подтверждает `catchError` + генерацию отчёта при падении; Telegram failure ещё не проверен. Локальный dry-run `notify-telegram.sh` (`DRY_RUN=1`, dummy env) подтвердил ветки `REPORT_PUBLISHED=true/false` и `ARTIFACTS_URL`.

**Открыто до закрытия фазы:** перенести/создать `telegram-bot-token` и `telegram-chat-id` в system scope (нужен администратор Jenkins), проверить Telegram success/failure; проверить assertion/compile-error/missing-results на реальном агенте; финальный `git diff --stat`.

## 4. Риски

**Baseline (2026-11-24):** `.\gradlew.bat test` (headed, локальный Chrome) — **зелёный**: `SearchTests.successfulSearchTest` 1/1, 8.9 с. В stderr: SLF4J NOP и `WARNING: Unable to find CDP implementation matching 158` (несовпадение CDP Selenium 4.49 и Chrome 158, не фатально).

| Риск | Вероятность | Влияние | Мера | Статус |
|---|---|---|---|---|
| `allure-junit5` несовместим с JUnit 6.1.x | низкая | высокое | использовать `allure-jupiter` 3.1.1 (валидирован JUnit 6.1.0); проверить на 6.1.3 прогоном | **закрыт**: 4/4 passed на JUnit 6.1.3 |
| Jenkins Allure plugin не поддерживает Allure 3 | низкая | среднее | plugin 3.0.0 поддерживает Allure 3; fallback — CLI + `publishHTML`/archive | решён |
| `github.com/search` отдаёт captcha/rate-limit в CI | **высокая** | высокое | четыре прогона 2026-11-24 зелёные (4/4 каждый); `baseUrl` уже переопределяем; при блокировке — smoke-тег/отдельный CI-таргет, без правки тестов | открыт |
| 4 поисковых запроса на прогон (2 теста + 2 параметра навигации) | средняя | высокое | не более одного поиска на тест; четыре прогона подряд зелёные (2026-11-24); при captcha — сократить параметризацию навигации до одного кейса | принят |
| Разметка github.com меняется (динамические CSS-классы, A/B-варианты выдачи) | высокая | среднее | локаторы только на `data-testid`/`aria-label`/текст; `results-list` отсутствует и в позитивной, и в негативной выдаче по-разному — негатив опирается на `search-sub-header` + отсутствие списка | принят |
| `repo-name-link` содержит только имя репозитория (`selenide`, `niffler`), без owner | низкая | низкое | в параметризации передаётся короткое имя для проверки, полное (`owner/repo`) — для поиска ссылки | решён |
| Поиск в headless может отдать иную сортировку/состав топ-результатов | средняя | среднее | проверяется наличие конкретной ссылки в выдаче, а не позиция элемента | принят |
| Gradle не пробрасывает `-D` в test JVM | высокая | среднее | в фазе 1 явно `test { systemProperty ... }` или `systemProperties System.getProperties()`; проверить `-Dselenide.headless=true` | **решён (фаза 1)**: явный список ключей в `tasks.withType(Test)`; headed/headless/`-DbaseUrl` проверены |
| Chrome 158 vs CDP Selenium 4.49 (warning в baseline) | средняя | низкое | warning не ломает тест; не включать CDP-фичи; при падении — обновление Selenide отдельным решением | принят |
| Allure results пишутся не туда (`allure-results` vs `build/allure-results`) | средняя | среднее | явно задать `allure.results.directory` в `build.gradle`/`TestBase`, проверить генерацию | **решён**: `build/allure-results`, 4 result JSON |
| Allure 3 `generate` без `-o` | средняя | низкое | проверить `npx allure generate --help`; иначе `allurerc.mjs` с `output` | **решён**: `-o/--output` есть, default `allure-report`; используем `allurerc.mjs` |
| `@Step` не попадает в отчёт без AspectJ weaver | высокая | низкое | `aspectjweaver` 1.9.27 + `-javaagent`; проверить result JSON | **решён**: шаги `Выполнить поиск` / `Открыть репозиторий...` в отчёте |
| `npx` тянет CLI из сети при каждом прогоне / Node < 22.18 | средняя | низкое | пин `allure@3.33.0`; кэша npm в CI нет, CLI скачивается заново; Node `22.23.5` предустановлен (≥ 22.18) | принят |
| Awesome-отчёт: `index.html` в корне output, а не в `awesome/` | низкая | низкое | открывать `build/allure-report`; не хардкодить `awesome/` в Jenkins/GHA | принят |
| Вложения раздувают отчёт (page source ~200 КБ на падение) | низкая | низкое | вложения только на `FAIL`; без скриншотов на каждый шаг | принят |
| `setup-java` мажор устарел (v4 → v5) | средняя | низкое | сверено: v1–v4 deprecated, используется v6 (v6.3.0); версия записана | **закрыт** |
| Chrome в `ubuntu-latest` обновляется, драйвер/Chrome расходятся | средняя | среднее | версии логируются; Selenium Manager добирает драйвер; при поломке — `setup-chrome@v2` с пином, без изменения Selenide | принят |
| Публикация отчёта для PR/форков | низкая | среднее | deploy только для `master` на `push`/`workflow_dispatch`; PR получает артефакт, но не Pages | принят |
| `workflow_dispatch` с не-master ref не публикует отчёт | низкая | низкое | осознанно: деплой только с `master`; для проверки запускать dispatch на `master` | принят |
| Секреты Telegram попадают в лог | низкая | высокое | secrets + маскирование (`***`) + отсутствие `echo`/`set -x`; `curl --silent`, URL с токеном не печатается | **закрыт (фаза 5)**: `token_like`/`api.telegram.org`/`chat_id=` = 0 в логах success и failure |
| Telegram API недоступен, 429 или неверный `chat_id` | средняя | низкое | 3 попытки, затем `::warning::` с HTTP-кодом и `description`, `exit 0`; сборка не падает | принят |
| Секреты не заданы (форк-PR, локальный прогон) | средняя | низкое | скрипт проверяет пустые env и выходит с `::notice::`/`exit 0`; проверено локальным запуском того же скрипта без секретов | принят |
| Jenkins-агенты на JDK 21, проект разрабатывался на Java 25 | высокая | среднее | Gradle 9.6 требует Java 17+; проверить прогон на агенте; при несовместимости — JDK-агент настраивает администратор, версии проекта не менять | **закрыт**: build #1 на JDK 21 — `SUCCESS`, 4/4 |
| Общий Selenoid недоступен/перегружен | средняя | высокое | health check перед прогоном не добавляем; при недоступности сборка падает; статус/браузеры проверить `/status` | принят; build #1 прошёл через Selenoid |
| Selenide логирует remote URL с credentials | средняя | высокое | Jenkins `withCredentials` маскирует полное значение; проверить консоль на `user:`/`chat_id`/токен; при утечке — снизить логирование Selenide или перейти на username/password credentials | **закрыт для Selenoid**: в консоли #1–#4 URL и `user:` отсутствуют; Telegram-секреты не печатаются |
| Нет прав на создание job/запуск в Jenkins | высокая | высокое | логин студента; job создаётся вручную или через API после проверки прав; `Jenkinsfile` хранится в репозитории | **закрыт**: job существует, build #2 `SUCCESS`; анонимный `404` — ограничение прав, не удаление job |
| Jenkins за Keycloak SSO: пароль не работает для API | высокая | высокое | создать Jenkins API token (Profile → Configure → API Token) и передать его вне репозитория; Basic с паролем возвращает `401` | **закрыт**: API token работает, Basic с паролем — `401` |
| `Jenkinsfile` отсутствует в remote `master`; job пока inline | высокая | среднее | запушить файл после явного разрешения (R7) и переключить job на Pipeline from SCM; до этого inline probe не считается финальным | **закрыт**: `e6a0f5e` в `origin/master`, job на SCM, build #4 `SUCCESS` |
| Telegram credentials в user store не видны pipeline | высокая | высокое | system-scope credentials (или folder scope) создаёт администратор; user-store `withCredentials` даёт `CredentialNotFoundException` | **открыт**: IDs `telegram-bot-token`/`telegram-chat-id` созданы в user store, но не видны; system store → `404` |
| JUnit XML не публикуется или теряется | средняя | высокое | `junit` по `build/test-results/test/*.xml`; отсутствие XML — явная ошибка и `FAILURE`; архивировать `test-results` | **закрыт для success**: build #3 — JUnit 4/4, XML и `reports` в артефактах; failure/compile pending |
| Allure CLI в Jenkins 3.13.0, локально/GHA 3.33.0 | низкая | низкое | версия приходит из installation `Allure 3`; отчёт 4/4 сгенерирован; зафиксировать расхождение, версии проекта не менять | принят |

## 5. Секреты и доступы (без значений)

| Имя | Где хранится | Владелец | Фаза |
|---|---|---|---|
| `TELEGRAM_BOT_TOKEN` | GitHub Secrets репозитория `kulchub-Aleksandr/getting-started-java` (задан 2026-11-24); Jenkins user-store credential `telegram-bot-token` (создан 2026-11-24; pipeline не видит — нужен system scope) | владелец репозитория / администратор Jenkins | 5–6 |
| `TELEGRAM_CHAT_ID` | GitHub Secrets репозитория `kulchub-Aleksandr/getting-started-java` (задан 2026-11-24); Jenkins user-store credential `telegram-chat-id` (создан 2026-11-24; pipeline не видит — нужен system scope) | владелец репозитория / администратор Jenkins | 5–6 |
| `selenoid-guest-webdriver-url` | Jenkins credentials, Secret text (существующий; полный WebDriver URL с `/wd/hub`); использован в build #1–#3 | администратор Jenkins | 6 |

`telegram-bot-token` и `telegram-chat-id` имеют точные ID и тип `Secret text`, значения не читались. `withCredentials` в build #3 вернул `CredentialNotFoundException`; system-store путь `/credentials/store/system/domain/_/credential/<id>/` → `404`, поэтому создание/перенос в system scope выполняет администратор Jenkins.

Значения в репозитории, workflow и документации отсутствуют; `.env` добавлен в `.gitignore`.

## 6. Артефакты и ссылки CI

| Артефакт | Где | Ссылка | Фаза |
|---|---|---|---|
| `build/allure-results` | artifact GHA **`allure-results`** / Jenkins | [run 45379934914](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45379934914/artifacts) | 3–6 |
| HTML-отчёт `build/allure-report` | GitHub Pages / Jenkins | https://kulchub-aleksandr.github.io/getting-started-java/ | 3–6 |
| Allure-отчёт в Jenkins | build #1, installation `Allure 3` (CLI 3.13.0) | https://jenkins.qa.guru/job/alekskulch-getting-started-java/1/allure/ | 6 |
| Workflow | `.github/workflows/tests.yml` | [run 45396414475](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45396414475) | 4 |
| Уведомление Telegram | шаг `Notify Telegram` + `.github/scripts/notify-telegram.sh` | run [45395989599](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45395989599) (success), [45396414475](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45396414475) (failure) | 5–6 |
| `Jenkinsfile` | корень (untracked; в remote `master` пока 404) | | 6 |

## 7. Журнал фаз

| Фаза | Статус | Дата | Заметки |
|---|---|---|---|
| 0. Аудит | **закрыт** | 2026-11-24 | Baseline зелёный; совместимость проверена; S1–S12 подтверждены |
| 1. TestBase | **закрыт** | 2026-11-24 | `TestConfig` + `TestBase`; `SearchTests` в пакете `tests`; headed/headless/`-DbaseUrl`/env/сброс к дефолту проверены |
| 2. Тесты + PageObject | **закрыт** | 2026-11-24 | 3 page-класса (композиция `$`/`$$`), 3 метода тестов / 4 выполнения; headless дважды зелёный (8.6 с и ~8 с); локаторов и `sleep` в `tests/` нет; TestBase/конфиг/build.gradle не менялись |
| 3. Allure 3 | **закрыт** | 2026-11-24 | `allure-bom`/`jupiter`/`selenide` 3.1.1 + aspectjweaver 1.9.27; результаты в `build/allure-results`; Awesome-отчёт в `build/allure-report`; 4/4 passed; контролируемая поломка → screenshot + page source, откат → зелёный |
| 4. GitHub Actions | **закрыт** | 2026-11-24 | Workflow `tests.yml`: push/PR/dispatch, concurrency, timeout 30, JDK 25 + Gradle cache; браузер — Selenium Manager + предустановленный Chrome 157; `allure-results` артефакт при любом исходе; Awesome-отчёт на Pages; 3 зелёных run, кэш hit, test job 57 → 52 → 30 с |
| 5. Telegram | **закрыт** | 2026-11-24 | `curl` к Bot API без сторонних action; `.github/scripts/notify-telegram.sh` + шаг `Notify Telegram` (`always() && !cancelled()`); HTML, падения из JUnit XML; в сообщении имя коммита (`head_commit.message`/`pull_request.title`, fallback — короткий SHA); success [45395989599](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45395989599), failure [45396414475](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45396414475), откат [45397540787](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45397540787); токена в логах нет; без секретов — notice + skip |
| 6. Jenkins | в работе | 2026-11-24 | `Jenkinsfile` в SCM (`e6a0f5e`), job на SCM; build #1–#4 `SUCCESS`, 4/4; build #4: JUnit 4/4, `test-results`/`reports`/Allure в артефактах, отчёт HTTP 200; Selenoid remote, секреты замаскированы; Telegram credentials есть в user store, но pipeline их не видит (`CredentialNotFoundException`), нужен system scope; assertion/compile/missing-results pending |
| 7. Ревью и README | открыт | | |

## 8. Фрагмент для README (подготовлен в фазе 3, вставляется в фазе 7)

```markdown
## Allure Report 3

Прогон и отчёт:

```bash
./gradlew clean test -Dselenide.headless=true
npx allure@3.33.0 generate build/allure-results --clean
npx allure@3.33.0 open build/allure-report
```

Требуется Node.js ≥ 22.18.0. Результаты — `build/allure-results`, HTML-отчёт — `build/allure-report` (конфиг `allurerc.mjs`). Настройки отчёта: `name` и `output`. Вложения (скриншот и page source) добавляются только при падении.
```

Для CI (фаза 4/6): архивировать `build/allure-results`; отчёт публиковать Allure-плагином (Jenkins 3.0.0) или шагом `npx allure@3.33.0 generate`; `build/` в git не коммитить — он в `.gitignore`.

**GitHub Actions (фаза 4)**: `.github/workflows/tests.yml` прогоняет `./gradlew clean test -Dselenide.headless=true` на Temurin 25, всегда выгружает артефакт `allure-results` и публикует HTML-отчёт на GitHub Pages: https://kulchub-aleksandr.github.io/getting-started-java/.

**Jenkins (фаза 6)**: `allure-jenkins-plugin` 3.0.0 заявляет Allure 3 и встроенный Node.js; при проблемах fallback — `archiveArtifacts 'build/allure-results/**'` + `npx allure@3.33.0 generate` на агенте с Node ≥ 22.18 + `publishHTML` на `build/allure-report`.
