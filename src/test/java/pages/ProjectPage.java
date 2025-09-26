package pages;

import io.qameta.allure.Feature;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static data.Elements.NEW_TEST_BTN;


public class ProjectPage extends BasePage {

    @Step("Проверка отображения созданного проекта")
    public ProjectPage checkCreatingProject(String project) {//проверка что после создания провекта отображена кнопка new test
        NEW_TEST_BTN.shouldBe(visible, Duration.ofSeconds(60));
        $(byText(project)).shouldBe(visible);//имя проекта отображено
        return this;
    }
}
