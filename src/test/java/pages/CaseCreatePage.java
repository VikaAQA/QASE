package pages;

import com.codeborne.selenide.SelenideElement;
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
    private final SelenideElement BEHAVIOR_SECTION = $(byText("Behavior"));
    private final String SAVE_BTN = "Save";
    private final DropDown dropDown = new DropDown();
    private final Input input = new Input();

    @Step("Проверка, что страница создания тест-кейса открыта")
    public CaseCreatePage isPageOpened() {
        BEHAVIOR_SECTION.shouldBe(visible, Duration.ofSeconds(30));
        disableBeforeUnloadHard();
        log.info("Страница создания тест-кейса успешно загрузилась");
        return this;
    }
     @Step("Заполнение формы тест-кейса данными: {testCase}")
    public CaseCreatePage fillCreateCaseForm(QaseTestCaseDto testCase) {
        $(TITLE_CASE_FIELD).setValue(testCase.getTitle());
        input.fillInTextArea("Description", testCase.getDescription());
        dropDown.select( "Status", testCase.getStatus());
        dropDown.select( "Severity",  testCase.getSeverity());
        dropDown.select("Type", testCase.getType());
        dropDown.select( "Priority", testCase.getPriority());
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
        return new RepositoryPage ();
    }
}

