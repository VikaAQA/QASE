package pages;

import dto.QaseTestCaseDto;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static data.Elements.BLOCK_CASE;
import static data.Elements.NEW_TEST_BTN;
import static org.testng.Assert.assertEquals;

@Log4j2
public class RepositoryPage extends BasePage {
    private final String TEST_CASE_XPATH = "//*[text()='%s']//ancestor::*[@data-suite-body-id]/ancestor::*[@class][1]/following-sibling::*[1]//*[text()='%s']";
    private final String PROJECT_URL = "/project";
    private final String TEST_CASES_AREA_CSS = "[data-suite-body-id]";
    private final String TEST_CASES_LIST_CSS = TEST_CASES_AREA_CSS + "[draggable]";
    private final String NAME_SUITE_CSS = "[title='%s']";

    @Step("Открытие репозитория проекта '{project}'")
    public RepositoryPage openRepository(String project) {
       log.info("Открываем страницу проекта: {}", project);
      open(PROJECT_URL + "/" + project);
      return this;
    }
    @Step("Проверка, что страница проекта '{project}' открыт")
    public RepositoryPage isPageOpened(){
        NEW_TEST_BTN.shouldBe(visible, Duration.ofSeconds(60));
        disableBeforeUnloadHard();
        log.info("Страница проекта успешно открыта");
        return this;
    }
    @Step("Проверка, что проект '{project}' успешно создан и отображается на странице")
    public RepositoryPage checkCreatingProject(String project) {
        NEW_TEST_BTN.shouldBe(visible, Duration.ofSeconds(60));
        $(byText("Create new case")).shouldBe(visible);
        $(byText(project)).shouldBe(visible);
        log.info("Проект '{}' успешно создан и отображается", project);
        return this;
    }
    @Step("Проверка, что тест-кейс успешно создан")
    public RepositoryPage checkThatTestCaseIsCreated(int countCase) {
        $(By.xpath(BLOCK_CASE)).shouldBe(visible, Duration.ofSeconds(60));
        assertEquals(getTestCasesCount(), countCase, "Тест-кейс не создан");
        return this;
    }
    @Step("Получение количества тест-кейсов на странице проекта")
    public int getTestCasesCount() {
        int count = $$(TEST_CASES_LIST_CSS).size();
        log.debug("Найдено тест-кейсов на странице: {}", count);
        return count;
    }
    @Step("Открыть страницу создания тест-кейса")
    public CaseCreatePage openCasePage() {
        NEW_TEST_BTN.shouldBe(visible, Duration.ofSeconds(60)).click();

        CaseCreatePage page = page(CaseCreatePage.class);
                  page. disableBeforeUnloadHard();;
        return page;
    }
    @Step("Открыть страницу создания тестового набора (Suite)")
    public SuitePage openSuitPage() {
        $(byText("Create new suite"))
                .shouldBe(visible, Duration.ofSeconds(30))
                .click();
        log.info("Открыта форма создания Suite");
        return new SuitePage();
    }
    @Step("Проверка, что Suite '{suiteName}' отображается на странице")
    public RepositoryPage shouldHaveSuite(String suiteName) {
        String locator = String.format(NAME_SUITE_CSS, suiteName);
        $(locator).shouldBe(visible, Duration.ofSeconds(30));
        log.info("Suite '{}' успешно отображается на странице", suiteName);
        return this;
    }
     @Step("Проверка, что тест-кейс '{testCaseTitle}' принадлежит сьюте '{suiteTitle}'")
    public boolean doesTestCaseBelongToSuite(String suiteTitle, String testCaseTitle) {
        return $x(String.format(TEST_CASE_XPATH, suiteTitle, testCaseTitle)).exists();
    }
}
