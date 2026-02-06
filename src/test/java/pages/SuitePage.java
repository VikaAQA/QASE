package pages;

import dto.QaseTestSuiteDto;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@Log4j2
public class SuitePage extends BasePage {

    @Step("Проверка, что форма создания Suite открыта")
        public SuitePage isPageOpened() {
        $(byText("Suite name")).shouldBe(visible);
        log.info("Открыта страница создания Suite");
        return this;
    }
    @Step("Заполнение формы создания Suite данными: {testSuite}")
    public SuitePage fillFormSuitePge(QaseTestSuiteDto testSuite) {
        $(By.id("title")).append(testSuite.getSuit_name());
        $(byText("Create")).click();
        log.info("Отправлена форма создания Suite");
        return this;
    }
}
