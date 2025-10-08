package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import dto.TestCase;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static data.Elements.NEW_TEST_BTN;
import static data.Elements.TITLE_CASE_TXT;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static wrappers.DropDawn.selectFromCustomDropdown;

@Log4j2
public class CasePage extends BasePage {
    private static final String TEST_CASES_AREA_CSS = "[data-suite-body-id]";
    private static final String TEST_CASES_LIST_CSS = TEST_CASES_AREA_CSS + "[draggable]";
    private final String TITLE_CASE_FIELD = "input[name='title']";
    private final String DESCRIPTION_CASE_FIELD = ".toastui-editor-ww-container .ProseMirror-trailingBreak:first-child";
    private final String DROPDAWN_XPATH = "//label[text()='%s']/following-sibling::div//span";
    private final String FIELD_IN_DROPDAWN = "//div[text()='%s']";
    private final String SAVE_BTN = "Save";
    private final ProjectPage projectPage = new ProjectPage();
    @Step("Открытие страницы создания тест-кейса")
    public CasePage isPageOpened() {
        NEW_TEST_BTN.shouldBe(visible, Duration.ofSeconds(60)).click();
        $(byText(TITLE_CASE_TXT)).shouldBe(visible);
        return new CasePage();
    }
    @Step("Заполнение формы тест-кейса данными: {testCase}")
    public void fillCreateCaseForm(TestCase testCase) {

        $(TITLE_CASE_FIELD).append(testCase.getTitle());
        $(DESCRIPTION_CASE_FIELD).append(testCase.getDescription());

        selectFromCustomDropdown(DROPDAWN_XPATH, "Status", FIELD_IN_DROPDAWN, testCase.getStatus());
        selectFromCustomDropdown(DROPDAWN_XPATH, "Severity", FIELD_IN_DROPDAWN, testCase.getSeverity());
        selectFromCustomDropdown(DROPDAWN_XPATH, "Type", FIELD_IN_DROPDAWN, testCase.getType());
        selectFromCustomDropdown(DROPDAWN_XPATH, "Priority", FIELD_IN_DROPDAWN, testCase.getPriority());

        log.info("Форма заполнена данными тест-кейса: {}", testCase);
    }
    @Step("Создание нового тест-кейса через UI")
       public CasePage openCreateCase(TestCase testCase) {
        isPageOpened();
        fillCreateCaseForm(testCase);
        $(byText(SAVE_BTN)).click();
        log.info("Форма создания кейса успешно сохранена");
        return new CasePage();
    }

    @Step("Добавление шагов в тест-кейс")
    public CasePage addStep() {
        $(byText("Add step")).click();

        fillProseMirrorField("action", "Step-text");
        fillProseMirrorField("data", "Data-text");
        fillProseMirrorField("expected_result", "Expected Result-text");

        return this;
    }
    @Step("Заполнение текстового поля ProseMirror: {name} значением '{text}'")
    private void fillProseMirrorField(String name, String text) {
        // Находим именно тот wysiwyg-блок, который идёт ПЕРЕД нужным input
        String xpath = String.format(
                "//input[contains(@name,'%s')]/preceding-sibling::div[contains(@class,'wysiwyg')]//div[@contenteditable='true']",
                name
        );

        SelenideElement editor = $x(xpath)
                .shouldBe(Condition.visible, Duration.ofSeconds(10));
    }
    @Step("Получение количества тест-кейсов на странице")
    public int getTestCasesCount() {
        return $$(TEST_CASES_LIST_CSS).size();
    }
    @Step("Проверка, что тест-кейс успешно создан")
    public CasePage checkThatTestCaseIsCreated() {
        assertEquals(getTestCasesCount(), 1, "Test Case is not created or more than 1 test cases were created");
        return this;
    }
    @Step("Проверка, что тест-кейс '{testCaseName}' принадлежит сьюте '{suiteName}'")
    public CasePage checkThatTestCaseBelongsToSuite(String suiteName, String testCaseName) {
        assertTrue(projectPage.doesTestCaseBelongToSuite(suiteName, testCaseName), "Test Case is not created or or belongs to another suite");
        return this;
    }
}
