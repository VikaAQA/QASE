package pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import dtos.TestCase;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.util.List;

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
    private final String TITLE_CASE_FIELD = "input[name='title']";
    private final String DESCRIPTION_CASE_FIELD = ".toastui-editor-ww-container .ProseMirror-trailingBreak:first-child";
    private final String DROPDAWN_XPATH = "//label[text()='%s']/following-sibling::div//span";
    private final String FIELD_IN_DROPDAWN = "//div[text()='%s']";
    private final String SAVE_BTN = "Save";
    private static final String TEST_CASES_AREA_CSS = "[data-suite-body-id]";

    private static final String TEST_CASES_LIST_CSS = TEST_CASES_AREA_CSS + "[draggable]";
    private final ProjectPage projectPage = new ProjectPage();

    public CasePage isPageOpened() {
        NEW_TEST_BTN.shouldBe(visible, Duration.ofSeconds(60)).click();
        $(byText(TITLE_CASE_TXT)).shouldBe(visible);
        return new CasePage();
    }

    public void fillCreateCaseForm(TestCase testCase){

        $(TITLE_CASE_FIELD).append(testCase.getTitle());
        $(DESCRIPTION_CASE_FIELD).append(testCase.getDescription());

        selectFromCustomDropdown(DROPDAWN_XPATH, "Status", FIELD_IN_DROPDAWN, testCase.getStatus());
        selectFromCustomDropdown(DROPDAWN_XPATH, "Severity", FIELD_IN_DROPDAWN, testCase.getSeverity());
        selectFromCustomDropdown(DROPDAWN_XPATH, "Type", FIELD_IN_DROPDAWN, testCase.getType());
        selectFromCustomDropdown(DROPDAWN_XPATH, "Priority", FIELD_IN_DROPDAWN, testCase.getPriority());
        log.info("Форма заполнена данными тест-кейса: {}", testCase); }

    @Step("Открытие формы создание кейса")
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
        List<String> text = List.of("Step-text", "Data-text", "Expected Result-text");

        // Находим контейнеры
        ElementsCollection containers = $$(".Es6DHW.gMuCDP.Hm_zG7.eZv6zA.wysiwyg.dcuUk8")
                .shouldHave(CollectionCondition.size(3));

        for (int i = 0; i < containers.size(); i++) {
            SelenideElement container = containers.get(i);

            // Для каждого контейнера используем свой подход
            SelenideElement field;

            if (i == 0) {
                // Первое поле - contenteditable (нужно активировать)
                field = container.$("div[contenteditable='true']")
                        .shouldBe(visible, Duration.ofSeconds(10));
                field.click();
            } else {
                // Второе и третье поле - обычный div с текстом
                field = container.$(".toastui-editor-contents")
                        .shouldBe(visible);
            }

            // Очищаем и вводим текст
            executeJavaScript("arguments[0].innerHTML = '';", field);
            field.append(text.get(i));
        }

        return this;
    }

    public int getTestCasesCount() {
        return $$(TEST_CASES_LIST_CSS).size();
    }

    public CasePage checkThatTestCaseIsCreated() {
        assertEquals(getTestCasesCount(), 1, "Test Case is not created or more than 1 test cases were created");
        return this;
    }

    public CasePage checkThatTestCaseBelongsToSuite(String suiteName, String testCaseName) {
        assertTrue(projectPage.doesTestCaseBelongToSuite(suiteName, testCaseName), "Test Case is not created or or belongs to another suite");
        return this;
    }
}
