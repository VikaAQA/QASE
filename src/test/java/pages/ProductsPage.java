package pages;


import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static data.Elements.CREATE_NEW_PROJECT_BTN;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ProductsPage extends BasePage {

    private final String PROJECTS_URL = "/projects";
    private final String REMOVE_BTN = "[data-testid=remove]";
    private final String DELETE_PROJECTS_BTN = "//span[text()='Delete project']";
    private final String OPEN_ACTION_MENU_BTN = "button[aria-label='Open action menu']";
    @Step("Открытие страницы проектов")
    public ProductsPage openPage() {
        open(PROJECTS_URL);
        log.info("Открыта страница проектов по URL: {}", PROJECTS_URL);
        return this;
    }
    @Step("Ожидание загрузки страницы проектов")
    public ProductsPage waittingOpen() {
        CREATE_NEW_PROJECT_BTN
                .shouldBe(visible, Duration.ofSeconds(90));
        log.info("Страница проектов успешно загружена и кнопка 'Create new project' отображается");
        return this;
    }
    @Step("Удаление проекта с именем: '{project}'")
    public ProductsPage deleteProject(String project) {
        log.info("Начало удаления проекта: {}", project);

        $(byText(project))
                .ancestor("tr")
                .find(OPEN_ACTION_MENU_BTN)
                .click();

        $(REMOVE_BTN).click();
        $x(DELETE_PROJECTS_BTN).click();

        log.info("Проект '{}' удалён", project);
        return this;
    }
}

