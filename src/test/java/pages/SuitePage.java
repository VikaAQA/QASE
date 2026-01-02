package pages;

import dto.QaseTestSuiteDto;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@Log4j2
public class SuitePage extends BasePage {

    private final String NAME_SUITE_CSS = "[title='%s']";

    public SuitePage openPage() {
        $(byText("Create new suite")).click();
        log.info("Открытие страницы создания Suite");
        return new SuitePage();
    }

    public SuitePage isPageOpened() {
        $(byText("Suite name")).shouldBe(visible);
        log.info("Открыта страница создания Suite");
        return this;
    }

    public SuitePage fillFormSuitePge(QaseTestSuiteDto testSuite) {
        $(By.id("title")).append(testSuite.getSuit_name());
        $(byText("Create")).click();
        return this;
    }

    public SuitePage shouldHaveSuite(String suiteName) {
        String locator = String.format(NAME_SUITE_CSS, suiteName);
        $(locator).shouldBe(visible);
        return this;
    }
}
