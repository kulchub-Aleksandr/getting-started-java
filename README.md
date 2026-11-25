# getting-started-java

Учебный проект курса qa.guru/ai-first: UI-автотесты на GitHub Search с полным CI-контуром
(TestBase → тесты → PageObject → Allure 3 → GitHub Actions → Telegram → Jenkins).

## Требования

| Компонент | Версия | Зачем |
|---|---|---|
| JDK | Temurin 25 (локально 25.0.5+8) | сборка и запуск тестов |
| Gradle | 9.6.0 (wrapper) | система сборки |
| Браузер | Google Chrome (локальный headed-прогон) | целевой браузер |
| Node.js | ≥ 22.18.0 | только для генерации Allure-отчёта (`npx allure`) |

Версии Selenide, JUnit, Allure и AspectJ зафиксированы в `build.gradle` и `docs/plans/decisions.md`.

## Запуск тестов локально

```bash
# headless (рекомендуется для CI и быстрой проверки)
./gradlew clean test -Dselenide.headless=true

# headed (нужен локальный Chrome)
./gradlew clean test
```

PowerShell (Windows):

```powershell
.\gradlew.bat clean test "-Dselenide.headless=true"
```

## Конфигурация

Приоритет значения для каждого ключа: **system property → env → значение по умолчанию**.

| Ключ | `-D` | Env | Default |
|---|---|---|---|
| `baseUrl` | `-DbaseUrl` (алиас `-Dselenide.baseUrl`) | `BASE_URL` | `https://github.com` |
| `browser` | `-Dbrowser` (алиас `-Dselenide.browser`) | `BROWSER` | `chrome` |
| `headless` | `-Dheadless` (алиас `-Dselenide.headless`) | `HEADLESS` | `false` |
| `browserSize` | `-DbrowserSize` (алиас `-Dselenide.browserSize`) | `BROWSER_SIZE` | `1920x1080` |
| `timeout` | `-Dtimeout=10s` / `-Dtimeout=500ms`; нативный `-Dselenide.timeout` — миллисекунды | `TIMEOUT` | `10s` |
| `remote` | `-Dremote` (алиас `-Dselenide.remote`) | `REMOTE` | пусто (локальный браузер) |

`timeout`: значение без единицы — секунды; суффикс `ms` — миллисекунды.

## Allure-отчёт

```bash
./gradlew clean test -Dselenide.headless=true
npx allure@3.33.0 generate build/allure-results --clean
npx allure@3.33.0 open build/allure-report
```

Одной командой (генерация + открытие):

```bash
npx allure@3.33.0 generate build/allure-results --clean --open
```

Результаты — `build/allure-results`, HTML-отчёт — `build/allure-report`
(конфиг `allurerc.mjs`: имя отчёта и каталог вывода). Вложения (скриншот и page source)
добавляются только при падении теста.

> Windows PowerShell: `npx` может быть заблокирован политикой выполнения.
> Используйте `npx.cmd` или `cmd /c "npx ..."`.

## CI

| Канал | Ссылка |
|---|---|
| GitHub Actions workflow | `.github/workflows/tests.yml` |
| Опубликованный Allure-отчёт | https://kulchub-aleksandr.github.io/getting-started-java/ |
| Jenkins | https://jenkins.qa.guru/job/alekskulch-getting-started-java/ (вход по Keycloak SSO; для API — Jenkins API token) |

## Секреты

Задаются только в GitHub Secrets / Jenkins Credentials, **никогда** в коде и логах.

| Имя | Где задавать |
|---|---|
| `TELEGRAM_BOT_TOKEN` | GitHub Secrets репозитория; Jenkins Credential `telegram-bot-token` |
| `TELEGRAM_CHAT_ID` | GitHub Secrets репозитория; Jenkins Credential `telegram-chat-id` |
| `selenoid-guest-webdriver-url` | Jenkins Credential (Secret text), полный WebDriver URL |

## Структура проекта

```text
src/test/java/
  config/TestConfig.java      # system property -> env -> default
  core/TestBase.java          # Configuration в @BeforeAll, closeWebDriver в @AfterAll
  pages/SearchPage.java       # страница поиска
  pages/SearchResultsPage.java# результаты поиска
  pages/RepositoryPage.java   # страница репозитория
  tests/SearchTests.java      # тесты (3 метода / 4 выполнения)
.github/workflows/tests.yml   # CI-прогон + публикация отчёта
.github/scripts/notify-telegram.sh # уведомление в Telegram (GHA и Jenkins)
Jenkinsfile                   # Jenkins pipeline
allurerc.mjs                  # конфиг Allure (имя и каталог отчёта)
```
