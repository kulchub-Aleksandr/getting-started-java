# Домашнее задание: CI-контур для getting-started-java

Дата отчёта: 2026-11-25.

## 1. Репозиторий

- Репозиторий: https://github.com/kulchub-Aleksandr/getting-started-java
- Ветка: `master`
- Итоговый коммит: [`c1cf8f4`](https://github.com/kulchub-Aleksandr/getting-started-java/commit/c1cf8f4) — phase 7 audit, README, `.gitignore`
- Журнал решений по фазам: [`docs/plans/decisions.md`](docs/plans/decisions.md)

Примечание: GitHub не помечает этот репозиторий как fork (`isFork=false`, родитель отсутствует). История фаз и артефакты находятся в этом репозитории; отдельного fork-репозитория с теми же фазами нет.

## 2. Коммиты по фазам

| Фаза | Коммит | Что сделано | Подтверждение |
|---|---|---|---|
| Подготовка | [`09be4d9`](https://github.com/kulchub-Aleksandr/getting-started-java/commit/09be4d9) | Удалены build-артефакты из git, добавлен `.gitignore` | чистая история без `build/` и `.gradle/` |
| Подготовка | [`299eca9`](https://github.com/kulchub-Aleksandr/getting-started-java/commit/299eca9) | Цель тестов переведена на GitHub Search, обновлены зависимости | baseline-прогон |
| 0. Аудит | [`5b9c5f2`](https://github.com/kulchub-Aleksandr/getting-started-java/commit/5b9c5f2) | Добавлены план фаз и заполненный `docs/plans/decisions.md` | решения и риски зафиксированы |
| 1. TestBase + конфиг | [`d7ca457`](https://github.com/kulchub-Aleksandr/getting-started-java/commit/d7ca457) | `TestConfig`, `TestBase`, проброс `-D`/env в тестовую JVM | headed, headless и `-DbaseUrl` проверены |
| 2. Тесты | [`a882922`](https://github.com/kulchub-Aleksandr/getting-started-java/commit/a882922) | 3 тестовых метода / 4 выполнения: успешный поиск, пустая выдача, переход в репозиторий (2 параметра) | 4/4 passed |
| 3. PageObject | [`a882922`](https://github.com/kulchub-Aleksandr/getting-started-java/commit/a882922) | `SearchPage`, `SearchResultsPage`, `RepositoryPage`; локаторы без XPath-простыней | тесты и page-слой внесены одним коммитом |
| 4. Allure 3 | [`eebcff3`](https://github.com/kulchub-Aleksandr/getting-started-java/commit/eebcff3) | Allure BOM/Jupiter/Selenide 3.1.1, AspectJ weaver, `allurerc.mjs`; результаты в `build/allure-results` | Awesome-отчёт, 4/4 passed, вложения только при падении |
| 5. GitHub Actions | [`baa920c`](https://github.com/kulchub-Aleksandr/getting-started-java/commit/baa920c) | Workflow `.github/workflows/tests.yml`: push/PR/dispatch, JDK 25, Chrome, artifact `allure-results` | первые зелёные run |
| 5. GitHub Actions | [`4022899`](https://github.com/kulchub-Aleksandr/getting-started-java/commit/4022899) | Публикация Allure на GitHub Pages для push/dispatch в `master` | отчёт на Pages |
| 5. GitHub Actions | [`4b79e86`](https://github.com/kulchub-Aleksandr/getting-started-java/commit/4b79e86) | Зафиксированы решения и ссылки фазы 4 | `decisions.md` |
| 6. Telegram | [`d50dc3c`](https://github.com/kulchub-Aleksandr/getting-started-java/commit/d50dc3c) | `.github/scripts/notify-telegram.sh`, шаг `Notify Telegram`, отправка при success/failure | success и failure run |
| 6. Telegram | [`837cce5`](https://github.com/kulchub-Aleksandr/getting-started-java/commit/837cce5) | В уведомлении показывается имя коммита вместо SHA | зелёный run |
| 6. Проверка падения CI | [`21c2b45`](https://github.com/kulchub-Aleksandr/getting-started-java/commit/21c2b45) | Временно сломан поисковый тест для проверки красного CI | контролируемое падение |
| 6. Проверка падения CI | [`d6b7e37`](https://github.com/kulchub-Aleksandr/getting-started-java/commit/d6b7e37) | Тест восстановлен, падение задокументировано | зелёный run |
| 6. Проверка Telegram | [`8755267`](https://github.com/kulchub-Aleksandr/getting-started-java/commit/8755267) | Временно сломан тест для проверки failure-уведомления | контролируемое падение |
| 6. Проверка Telegram | [`74a622b`](https://github.com/kulchub-Aleksandr/getting-started-java/commit/74a622b) | Тест восстановлен, обновлены решения фазы 5 | зелёный run |
| 6. Проверка Telegram | [`afffddf`](https://github.com/kulchub-Aleksandr/getting-started-java/commit/afffddf) | Зафиксированы проверочные запуски | `decisions.md` |
| 6. Проверка Telegram | [`75332b6`](https://github.com/kulchub-Aleksandr/getting-started-java/commit/75332b6) | Уточнена проверка логов на отсутствие секретов | `decisions.md` |
| 7. Jenkins | [`e6a0f5e`](https://github.com/kulchub-Aleksandr/getting-started-java/commit/e6a0f5e) | `Jenkinsfile`: Checkout → Test → Report → Publish, JUnit, Allure 3, Selenoid через credentials | Jenkins build #4 |
| 7. Jenkins | [`4b8e0ea`](https://github.com/kulchub-Aleksandr/getting-started-java/commit/4b8e0ea) | Зафиксирован переход job с inline на Pipeline from SCM | Jenkins build #4 |
| 7. Jenkins | [`cf51471`](https://github.com/kulchub-Aleksandr/getting-started-java/commit/cf51471) | Исправлена проверка наличия JUnit XML (убран `| head`, маскировавший exit code) | Jenkins build #5 |
| 7. Jenkins | [`11955c5`](https://github.com/kulchub-Aleksandr/getting-started-java/commit/11955c5) | Задокументирована матрица success/assertion/compile/missing-results | `decisions.md` |
| 7. Jenkins | [`3ae4bee`](https://github.com/kulchub-Aleksandr/getting-started-java/commit/3ae4bee) | Обновлён prompt фазы Jenkins | docs-only |
| 7. Ревью и README | [`c1cf8f4`](https://github.com/kulchub-Aleksandr/getting-started-java/commit/c1cf8f4) | Независимый аудит, README, `.idea/` в `.gitignore` | финальный зелёный CI |

Фактическая особенность истории: тесты и PageObject внесены одним коммитом `a882922`; отдельного коммита только под PageObject нет. Остальные фазы разделены логическими коммитами.

## 3. GitHub Actions: реальные запуски

Workflow: [`.github/workflows/tests.yml`](.github/workflows/tests.yml). Все ссылки ниже ведут на реальные run-ы.

| Фаза | Коммит | Run | Результат | Что подтверждено |
|---|---|---|---|---|
| 5. GHA | `baa920c` | [45379157672](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45379157672) | **SUCCESS** | workflow добавлен, тесты прошли |
| 5. GHA | `baa920c` | [45379628682](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45379628682) | **SUCCESS** | ручной `workflow_dispatch` |
| 5. GHA | `4022899` | [45379934914](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45379934914) | **SUCCESS** | артефакт `allure-results`, публикация отчёта |
| 5. GHA | `4b79e86` | [45380606861](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45380606861) | **SUCCESS** | документирование CI |
| 5. Контролируемое падение | `21c2b45` | [45384436473](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45384436473) | **FAILURE** | красный CI на сломанном тесте |
| 5. Восстановление | `d6b7e37` | [45385108778](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45385108778) | **SUCCESS** | откат поломки, снова 4/4 |
| 6. Telegram success | `d50dc3c` | [45395989599](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45395989599) | **SUCCESS** | уведомление при успехе |
| 6. Контролируемое падение | `8755267` | [45396414475](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45396414475) | **FAILURE** | уведомление при падении |
| 6. Восстановление | `74a622b` | [45397540787](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45397540787) | **SUCCESS** | откат и зелёный прогон |
| 6. Проверка | `afffddf` | [45397918874](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45397918874) | **SUCCESS** | проверочный запуск |
| 6. Проверка | `75332b6` | [45398423239](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45398423239) | **SUCCESS** | проверка логов |
| 6. Telegram | `837cce5` | [45400958011](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45400958011) | **SUCCESS** | имя коммита в уведомлении |
| 7. Jenkins-код | `e6a0f5e` | [45451312499](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45451312499) | **SUCCESS** | GHA не сломан добавлением `Jenkinsfile` |
| 7. Jenkins SCM | `4b8e0ea` | [45452346936](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45452346936) | **SUCCESS** | GHA-прогон после docs-коммита |
| 7. Jenkins fix | `cf51471` | [45459009245](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45459009245) | **SUCCESS** | GHA-прогон после исправления |
| 7. Jenkins matrix | `11955c5` | [45461089077](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45461089077) | **SUCCESS** | GHA-прогон после документирования матрицы |
| 7. Jenkins docs | `3ae4bee` | [45465146085](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45465146085) | **SUCCESS** | docs-only изменение |
| 7. Финальный | `c1cf8f4` | [45561326331](https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45561326331) | **SUCCESS** | job `test` + job `deploy`, отчёт опубликован |

Опубликованный Allure-отчёт: https://kulchub-aleksandr.github.io/getting-started-java/ (проверен, HTTP 200).
Артефакты последнего run: https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45561326331/artifacts

## 4. Jenkins: реальные сборки (фаза 7)

Job: https://jenkins.qa.guru/job/alekskulch-getting-started-java/
Доступ: Keycloak SSO; для API нужен Jenkins API token. Анонимный запрос к job/build возвращает `404`, поэтому ссылки ниже открываются только после входа.

| Build | Коммит / режим | Результат | Артефакты и проверки |
|---|---|---|---|
| [#1](https://jenkins.qa.guru/job/alekskulch-getting-started-java/1/) | inline probe | **SUCCESS** | 4/4 passed, Selenoid, `BUILD SUCCESSFUL in 52s` |
| [#2](https://jenkins.qa.guru/job/alekskulch-getting-started-java/2/) | inline probe | **SUCCESS** | 4/4 passed, Allure CLI 3.13.0, отчёт открывается |
| [#3](https://jenkins.qa.guru/job/alekskulch-getting-started-java/3/) | inline probe | **SUCCESS** | JUnit `passCount=4`, 79 артефактов, Allure 4/4 |
| [#4](https://jenkins.qa.guru/job/alekskulch-getting-started-java/4/) | SCM, `e6a0f5e` | **SUCCESS** | Pipeline from SCM, JUnit 4/0, Allure 4/4 |
| [#5](https://jenkins.qa.guru/job/alekskulch-getting-started-java/5/) | SCM, `cf51471` | **SUCCESS** | JUnit 4/0, Allure 4/4, 79 артефактов, секретов в консоли нет |

Allure-отчёт Jenkins build #5: https://jenkins.qa.guru/job/alekskulch-getting-started-java/5/allure/

### Контролируемые падения в Jenkins

Матрица проверялась на реальном агенте `java-jdk21` через временный job `alekskulch-getting-started-java-probe`. После проверки probe job был удалён как временный, поэтому прямые ссылки на его сборки больше не открываются (`404`). Факты и номера сборок сохранены в [`docs/plans/decisions.md`](docs/plans/decisions.md).

| Сценарий | Сборка | Результат | Что подтверждено |
|---|---|---|---|
| Success | основной build #5 | **SUCCESS** | JUnit 4/4, Allure 4/4, HTML и артефакты, `/5/allure/` HTTP 200 |
| Assertion failure | probe #5 | **FAILURE** | JUnit 3 passed / 1 failed, Allure `failed`, вложения: screenshot + page source |
| Compile error | probe #4 | **FAILURE** | `compileTestJava FAILED`, JUnit XML отсутствует, архив сохранён, `Publish` падает на «JUnit XML not found» |
| Missing results | probe #3 | **FAILURE** | тесты 4/4, но `build/test-results/test` удалён; JUnit XML отсутствует, зелёного статуса нет |
| Intentional failure test | временный job с `IntentionalFailureTest` | **FAILURE** | 5 tests / 1 failed, Allure `failed` (4 passed / 1 failed), отчёт HTTP 200 |

Исторические URL удалённого probe job (сейчас `404`, приведены для трассировки): `https://jenkins.qa.guru/job/alekskulch-getting-started-java-probe/3/`, `/4/`, `/5/`.

Дефект проверки, найденный в этой матрице: `ls .../*.xml | head` возвращал код `head` и мог не обнаруживать отсутствие XML. Исправлено в `cf51471`: проверка через `ls 'dir'/*.xml >/dev/null 2>&1` и `find ... | grep -q .` для `allure-results`.

## 5. Итоговые артефакты

| Артефакт | Ссылка / путь |
|---|---|
| GitHub Actions workflow | [`.github/workflows/tests.yml`](.github/workflows/tests.yml) |
| Опубликованный Allure-отчёт | https://kulchub-aleksandr.github.io/getting-started-java/ |
| Артефакт `allure-results` | https://github.com/kulchub-Aleksandr/getting-started-java/actions/runs/45561326331/artifacts |
| Jenkins job | https://jenkins.qa.guru/job/alekskulch-getting-started-java/ |
| Jenkins Allure build #5 | https://jenkins.qa.guru/job/alekskulch-getting-started-java/5/allure/ |
| JUnit XML в Jenkins | `build/test-results/test/TEST-tests.SearchTests.xml` (build #5) |
| Скрипт Telegram | [`.github/scripts/notify-telegram.sh`](.github/scripts/notify-telegram.sh) |
| Jenkins pipeline | [`Jenkinsfile`](Jenkinsfile) |
| Журнал решений | [`docs/plans/decisions.md`](docs/plans/decisions.md) |
| README | [`README.md`](README.md) |

## 6. Ограничения и честные оговорки

1. **Репозиторий не является GitHub-fork.** `gh api` возвращает `fork=false`, `parent=null`. Ссылка на собственный репозиторий с фазами указана в разделе 1.
2. **Ссылки на контролируемые падения Jenkins не сохранились.** Они выполнялись во временном probe job, который удалён после проверки; в отчёте приведены номера сборок и зафиксированные результаты из `decisions.md`. Success-сборки #1–#5 доступны по ссылкам.
3. **Telegram из Jenkins не доставлен.** Pipeline пропускает уведомление с `CredentialNotFoundException`: credentials `telegram-bot-token` и `telegram-chat-id` созданы в user store и не видны pipeline; нужен перенос в system scope администратором Jenkins. В GitHub Actions Telegram работает (success и failure run выше). Это единственный незакрытый пункт Jenkins-фазы.
4. **Цель тестов — `github.com/search`.** Риск rate-limit/captcha остаётся открытым; на момент отчёта все прогоны GHA и Jenkins были зелёными.
