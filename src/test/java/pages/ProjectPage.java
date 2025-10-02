package pages;

import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static data.Elements.NEW_TEST_BTN;


public class ProjectPage extends BasePage {
    private static String PROJECT_URL = "/project";
    private static final String TEST_CASE_XPATH = "//*[text()='%s']//ancestor::*[@data-suite-body-id]/ancestor::*[@class][1]/following-sibling::*[1]//*[text()='%s']";

    @Step("Проверка отображения созданного проекта")
    public ProjectPage checkCreatingProject(String project) {//проверка что после создания провекта отображена кнопка new test
        NEW_TEST_BTN.shouldBe(visible, Duration.ofSeconds(60));
        $(byText(project)).shouldBe(visible);//имя проекта отображено
        return this;
    }

    @Step("Oткрыт репозиторий проекта")
    public ProjectPage openRepository(String project){
open (PROJECT_URL+"/"+ project);
        NEW_TEST_BTN.shouldBe(visible, Duration.ofSeconds(60));
    return this;
    }

    public boolean doesTestCaseBelongToSuite(String suiteTitle, String testCaseTitle) {
        return $x(String.format(TEST_CASE_XPATH, suiteTitle, testCaseTitle)).exists();
    }
}
