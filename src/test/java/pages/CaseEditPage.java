package pages;

import dto.QaseTestCaseDto;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import wrappers.DropDown;
import wrappers.Input;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static data.Elements.TITLE_CASE_TXT;
import static org.testng.Assert.assertEquals;

@Log4j2
public class CaseEditPage extends BasePage{
    private final DropDown dropDown = new DropDown();
    private final Input input = new Input();


    @Step("Открыть страницу редактирования тест-кейса №{index} в проекте {projectCode}")
    public CaseEditPage openEditCasePage(String projectCode, int index) {
        open(String.format("/case/%s/edit/%s", projectCode, index));
        return this;
    }

    @Step("Проверка, что страница редактирования тест-кейса открыта")
    public CaseEditPage isPageOpened() {
        $(byText(TITLE_CASE_TXT)).shouldBe(visible, Duration.ofSeconds(30));
        disableBeforeUnloadSafe();
        log.info("Страница редактирования тест-кейса успешно загрузилась");
        return this;}

       /* @Step("Проверка заполнения полей тест-кейса на форме редактирования")
        public void assertEditFormMatchesTestCase(QaseTestCaseDto testCase) {
            log.info("Проверяем заполнение полей тест-кейса");
            assertEquals(getTestCaseSpecs(), testCase, "Характеристики тест-кейса указаны неверно");
            }*/
    @Step("Получение значений всех полей тест-кейса с формы")
    public QaseTestCaseDto getTestCaseSpecs() {
        return QaseTestCaseDto.builder().
                title($(By.name("title")).getValue()).
                status(dropDown.getPickListText("Status")).
                description(input.getTextAreaText("Description")).
                severity(dropDown.getPickListText("Severity")).
                priority(dropDown.getPickListText("Priority")).
                type(dropDown.getPickListText("Type")).
                isFlaky(dropDown.getPickListText("Is flaky").equals("Yes")).
                behavior(dropDown.getPickListText("Behavior")).
                automationStatus(dropDown.getPickListText("Automation status")).
                build();
    }
 }

