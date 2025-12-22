package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import dto.TestCase;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import wrappers.DropDawn;
import wrappers.Input;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static data.Elements.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Log4j2
public class CasePage extends BasePage {
    private static final String TEST_CASES_AREA_CSS = "[data-suite-body-id]";
    private static final String TEST_CASES_LIST_CSS = TEST_CASES_AREA_CSS + "[draggable]";
    private final String TITLE_CASE_FIELD = "input[name='title']";
    private final String DESCRIPTION_CASE_FIELD = "div[data-lexical-editor='true'][contenteditable='true']";
    private final String DROPDAWN_XPATH = "//label[text()='%s']/following-sibling::div//span";
    private final String FIELD_IN_DROPDAWN = "//div[text()='%s']";
    private final String SAVE_BTN = "Save";
    private final ProjectPage projectPage = new ProjectPage();

    @Step("Открытие страницы создания тест-кейса")
    public CasePage openPage() {
        NEW_TEST_BTN.shouldBe(visible, Duration.ofSeconds(60)).click();
        disableBeforeUnloadSafe();
        log.info("Страница создания тест-кейса открыта");
        return new CasePage();
    }

    @Step("Проверка, что страница создания тест-кейса открыта")
    public CasePage isPageOpened() {
        $(byText(TITLE_CASE_TXT)).shouldBe(visible, Duration.ofSeconds(30));
        log.info("Страница создания тест-кейса успешно открыта");
        return this;
    }

    public CasePage openEditCasePage(String projectCode, int testCaseIndex) {
        open(String.format("/case/%s/edit/%s", projectCode, testCaseIndex));
        return this;
    }

    @Step("Open test case number '{index}' of project '{projectCode}'")
    public CasePage openTestCase(String projectCode, int index) {
        log.info("Открытие тест-кейса '{}' в проектеt '{}'", index, projectCode);
        openEditCasePage(projectCode, index);
        return this;
    }

    @Step("Заполнение формы тест-кейса данными: {testCase}")
    public void fillCreateCaseForm(TestCase testCase) {
        dropDawn = new DropDawn();
        input = new Input();
        $(TITLE_CASE_FIELD).setValue(testCase.getTitle());
         // disableBeforeUnloadSafe();

        // Description — Lexical editor
        SelenideElement descriptionEditor =
                $(DESCRIPTION_CASE_FIELD)
                        .shouldBe(visible, Duration.ofSeconds(20));

        descriptionEditor.click();
        descriptionEditor.sendKeys(Keys.CONTROL + "a");
        descriptionEditor.sendKeys(Keys.DELETE);
        descriptionEditor.sendKeys(testCase.getDescription());

     // disableBeforeUnloadSafe();
        dropDawn.selectFromCustomDropdown(DROPDAWN_XPATH, "Status", FIELD_IN_DROPDAWN, testCase.getStatus());
        dropDawn.selectFromCustomDropdown(DROPDAWN_XPATH, "Severity", FIELD_IN_DROPDAWN, testCase.getSeverity());
        dropDawn.selectFromCustomDropdown(DROPDAWN_XPATH, "Type", FIELD_IN_DROPDAWN, testCase.getType());
        dropDawn.selectFromCustomDropdown(DROPDAWN_XPATH, "Priority", FIELD_IN_DROPDAWN, testCase.getPriority());
        if (testCase.isFlaky())
            dropDawn.select("Is flaky", "Yes"); // NO is default value
        dropDawn.select("Behavior", testCase.getBehavior());
        dropDawn.select("Automation status", testCase.getAutomationStatus());
        log.info("Форма заполнена данными тест-кейса: {}", testCase);
    }

    @Step("Создание нового тест-кейса через UI")
    public CasePage creattingTestCase(TestCase testCase) {
        openPage().isPageOpened();
        //disableBeforeUnload();
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
    public CasePage checkThatTestCaseIsCreated(int countCase) {
        $(By.xpath(BLOCK_CASE)).shouldBe(visible, Duration.ofSeconds(60));
        assertEquals(getTestCasesCount(), countCase, "Test Case is not created or more than 1 test cases were created");
        return this;
    }

    @Step("Проверка, что тест-кейс '{testCaseName}' принадлежит сьюте '{suiteName}'")
    public CasePage checkThatTestCaseBelongsToSuite(String suiteName, String testCaseName) {
        assertTrue(projectPage.doesTestCaseBelongToSuite(suiteName, testCaseName), "Test Case is not created or or belongs to another suite");
        return this;
    }

    @Step("Получение значений всех полей тест-кейса с формы")
    public TestCase getTestCaseSpecs() {
        dropDawn = new DropDawn();
        input = new Input();

        return TestCase.builder().
                title($(By.name("title")).getValue()).
                status(dropDawn.getPickListText("Status")).
              description(input.getTextAreaText("Description")).
                severity(dropDawn.getPickListText("Severity")).
                priority(dropDawn.getPickListText("Priority")).
                type(dropDawn.getPickListText("Type")).
                isFlaky(dropDawn.getPickListText("Is flaky").equals("Yes")).
                behavior(dropDawn.getPickListText("Behavior")).
                automationStatus(dropDawn.getPickListText("Automation status")).
                build();
    }

    @Step("Проверка заполнения полей тест-кейса")
    public CasePage checkTestCaseSpecs(TestCase testCase) {
        log.info("Проверяем заполнение полей тест-кейса");
        assertEquals(getTestCaseSpecs(), testCase, "Характеристики тест-кейса указаны неверно");
        return this;
    }
}

