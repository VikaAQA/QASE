package pages;

import dto.QaseTestCaseDto;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import wrappers.DropDown;
import wrappers.Input;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static data.Elements.*;

@Log4j2
public class CaseCreatePage extends BasePage {

    private final String TITLE_CASE_FIELD = "input[name='title']";
    private final String DESCRIPTION_CASE_FIELD = "div[data-lexical-editor='true'][contenteditable='true']";
    private final String DROPDOWN_XPATH = "//label[text()='%s']/following-sibling::div//span";
    private final String FIELD_IN_DROPDOWN = "//div[text()='%s']";
    private final String SAVE_BTN = "Save";
    private final DropDown dropDown = new DropDown();
    private final Input input = new Input();

    @Step("Проверка, что страница создания тест-кейса открыта")
    public CaseCreatePage isPageOpened() {
        $(byText(TITLE_CASE_TXT)).shouldBe(visible, Duration.ofSeconds(30));
        disableBeforeUnloadSafe();
        log.info("Страница создания тест-кейса успешно загрузилась");
        return this;
    }
     @Step("Заполнение формы тест-кейса данными: {testCase}")
    public CaseCreatePage fillCreateCaseForm(QaseTestCaseDto testCase) {
        $(TITLE_CASE_FIELD).setValue(testCase.getTitle());
         dismissAlertIfPresent();
        input.setTextInLexicalEditor(DESCRIPTION_CASE_FIELD,testCase.getDescription());
        dropDown.selectFromCustomDropdown(DROPDOWN_XPATH, "Status", FIELD_IN_DROPDOWN, testCase.getStatus());
        dropDown.selectFromCustomDropdown(DROPDOWN_XPATH, "Severity", FIELD_IN_DROPDOWN, testCase.getSeverity());
        dropDown.selectFromCustomDropdown(DROPDOWN_XPATH, "Type", FIELD_IN_DROPDOWN, testCase.getType());
        dropDown.selectFromCustomDropdown(DROPDOWN_XPATH, "Priority", FIELD_IN_DROPDOWN, testCase.getPriority());
        if (testCase.isFlaky())
            dropDown.select("Is flaky", "Yes");
        dropDown.select("Behavior", testCase.getBehavior());
        dropDown.select("Automation status", testCase.getAutomationStatus());
        log.info("Форма заполнена данными тест-кейса: {}", testCase);
        return this;
    }

    @Step("Нажатие кнопки Save")
    public RepositoryPage clickSave() {
        $(byText(SAVE_BTN)).click();
        log.info("Нажата кнопка Save");
        return new RepositoryPage();
    }
}

