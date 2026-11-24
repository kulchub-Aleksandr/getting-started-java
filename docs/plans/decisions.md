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
| Allure CLI | npm `allure` **3.33.0** (`npx allure`) | [registry.npmjs.org/allure](https://registry.npmjs.org/allure/latest); зафиксировать в CI | 3 |
| GitHub Actions | `ubuntu-latest` + `actions/setup-java` (Temurin 25) | Chrome/ChromeDriver предустановлены; мажор `setup-java` требует проверки | 4 |
| Jenkins | `allure-jenkins-plugin` **3.0.0** (требует Jenkins ≥ 2.541.3) | [plugins.jenkins.io](https://plugins.jenkins.io/allure-jenkins-plugin/) | 6 |

### 2.1 Совместимость (проверено 2026-11-24)

| Вопрос | Вердикт | Источник | Влияние на план |
|---|---|---|---|
| `allure-junit5` × JUnit 6.1.3 | **Да, но артефакт заменён**: `allure-junit5` в 3.x не публикуется, использовать `allure-jupiter` 3.1.1 (валидирован против JUnit 6.1.0 platform API) | [allure-jupiter README](https://github.com/allure-framework/allure-java/blob/main/allure-jupiter/README.md) | Фаза 3: подключать `allure-jupiter`, не `allure-junit5`; точную 6.1.3 проверить прогоном |
| `allure-selenide` × Allure 3 | **Да**: `allure-selenide` 3.1.1, targets Selenide 7.x (сборка валидирована на 7.17.0; у нас 7.18.2) | [allure-selenide README](https://github.com/allure-framework/allure-java/blob/main/allure-selenide/README.md) | Фаза 3: регистрировать `AllureSelenide` через `SelenideLogger` в `TestBase` |
| Allure Report 3: установка CLI | **Да**: npm-пакет `allure` 3.33.0, `npm i -g allure` или `npx allure`; результаты — каталог с именем `allure-results` | [Allure 3 README](https://github.com/allure-framework/allure3/blob/main/README.md), [npm](https://www.npmjs.com/package/allure) | Фаза 3: `npx allure generate <resultsDir>` / `npx allure open <reportDir>`; output задаётся в `allurerc.*` |
| Allure 3 `generate`: флаг `-o/--output` | **Требует проверки**: документирован вывод через `allurerc.*`; наличие `-o` у `generate` не подтверждено | [Allure 3 README](https://github.com/allure-framework/allure3/blob/main/README.md) | Фаза 3: дешёво проверить `npx allure generate --help`; при отсутствии — конфиг `allurerc.mjs` |
| Selenide 7.18.2 × Java 25 | **Да (эмпирически)**: baseline-прогон зелёный на Java 25.0.5; официального заявления про Java 25 нет | `build/test-results/test/TEST-SearchTests.xml`; [Selenide 7.18.2](https://github.com/selenide/selenide/releases/tag/v7.18.2) | Ограничений не вносим; при проблемах в CI — отдельное решение |
| Jenkins Allure plugin × Allure 3 | **Да**: plugin 3.0.0, «Recommended Allure 3» по умолчанию, встроенный Node.js | [plugins.jenkins.io/allure-jenkins-plugin](https://plugins.jenkins.io/allure-jenkins-plugin/) | Фаза 6: использовать plugin 3.0.0; fallback CLI + `publishHTML` не обязателен |
| GHA: Chrome/драйвер на `ubuntu-latest` | **Да, ставить не нужно**: образ (24.04/26.04) содержит Google Chrome 157 + ChromeDriver 157 + Selenium server 4.52 | [Ubuntu 26.04](https://github.com/actions/runner-images/blob/main/images/ubuntu/Ubuntu2604-Readme.md), [Ubuntu 24.04](https://github.com/actions/runner-images/blob/main/images/ubuntu/Ubuntu2404-Readme.md) | Фаза 4: без шага установки браузера; `browser-actions/setup-chrome@v2` — только для пина версии |
| Selenium Manager vs setup-action | **Selenium Manager** встроен в Selenium 4.6+/4.49; сам находит/скачивает драйвер и браузер; `setup-chrome@v2` ставит конкретную версию Chrome + совместимый ChromeDriver | [Selenium Manager](https://www.selenium.dev/documentation/selenium_manager/), [setup-chrome](https://github.com/browser-actions/setup-chrome) | Фаза 4: по умолчанию Selenium Manager + предустановленный Chrome; setup-action — если нужен пин |

## 3. Целевая структура файлов

```text
src/test/java/
  config/TestConfig.java          # system property -> env -> default
  core/TestBase.java              # Configuration, AllureSelenide, @AfterAll closeWebDriver
  pages/SearchPage.java           # композиция $/$$, @Step, fluent
  pages/SearchResultsPage.java    # результаты поиска (если понадобится)
  tests/SearchTests.java          # существует; перенос в пакет tests
src/test/resources/
  logback-test.xml                # опционально, чтобы убрать SLF4J NOP (согласовать)
.github/workflows/
  tests.yml                       # фаза 4
Jenkinsfile                       # фаза 6
allurerc.mjs                      # фаза 3 (output/plugins), если не хватит CLI-флагов
```

## 4. Риски

**Baseline (2026-11-24):** `.\gradlew.bat test` (headed, локальный Chrome) — **зелёный**: `SearchTests.successfulSearchTest` 1/1, 8.9 с. В stderr: SLF4J NOP и `WARNING: Unable to find CDP implementation matching 158` (несовпадение CDP Selenium 4.49 и Chrome 158, не фатально).

| Риск | Вероятность | Влияние | Мера | Статус |
|---|---|---|---|---|
| `allure-junit5` несовместим с JUnit 6.1.x | низкая | высокое | использовать `allure-jupiter` 3.1.1 (валидирован JUnit 6.1.0); проверить на 6.1.3 прогоном | решён (осталась проверка прогоном) |
| Jenkins Allure plugin не поддерживает Allure 3 | низкая | среднее | plugin 3.0.0 поддерживает Allure 3; fallback — CLI + `publishHTML`/archive | решён |
| `github.com/search` отдаёт captcha/rate-limit в CI | **высокая** | высокое | headless, переопределяемый `baseUrl`, smoke-тег, минимум запросов; при блокировке — другой target | открыт |
| Gradle не пробрасывает `-D` в test JVM | высокая | среднее | в фазе 1 явно `test { systemProperty ... }` или `systemProperties System.getProperties()`; проверить `-Dselenide.headless=true` | открыт |
| Chrome 158 vs CDP Selenium 4.49 (warning в baseline) | средняя | низкое | warning не ломает тест; не включать CDP-фичи; при падении — обновление Selenide отдельным решением | принят |
| Allure results пишутся не туда (`allure-results` vs `build/allure-results`) | средняя | среднее | явно задать `allure.results.directory` в `build.gradle`/`TestBase`, проверить генерацию | открыт |
| Allure 3 `generate` без `-o` | средняя | низкое | проверить `npx allure generate --help`; иначе `allurerc.mjs` с `output` | открыт |
| `setup-java` мажор устарел (v4 → v5) | средняя | низкое | сверить на фазе 4 с Marketplace; записать версию | открыт |
| Секреты Telegram попадают в лог | низкая | высокое | secrets + маскирование + отсутствие echo | открыт |

## 5. Секреты и доступы (без значений)

| Имя | Где хранится | Владелец | Фаза |
|---|---|---|---|
| `TELEGRAM_BOT_TOKEN` | GitHub Secrets / Jenkins Credentials | | 5–6 |
| `TELEGRAM_CHAT_ID` | GitHub Secrets / Jenkins Credentials | | 5–6 |

## 6. Артефакты и ссылки CI

| Артефакт | Где | Ссылка | Фаза |
|---|---|---|---|
| `allure-results` | artifact GHA / Jenkins | | 4, 6 |
| HTML-отчёт | gh-pages / Jenkins | | 4, 6 |
| Workflow | `.github/workflows/` | | 4 |
| `Jenkinsfile` | корень | | 6 |

## 7. Журнал фаз

| Фаза | Статус | Дата | Заметки |
|---|---|---|---|
| 0. Аудит | **закрыт** | 2026-11-24 | Baseline зелёный; совместимость проверена; S1–S12 подтверждены |
| 1. TestBase | открыт | | |
| 2. Тесты + PageObject | открыт | | |
| 3. Allure 3 | открыт | | |
| 4. GitHub Actions | открыт | | |
| 5. Telegram | открыт | | |
| 6. Jenkins | открыт | | |
| 7. Ревью и README | открыт | | |
