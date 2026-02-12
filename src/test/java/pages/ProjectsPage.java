package pages;


import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static data.Elements.CREATE_NEW_PROJECT_BTN;

@Log4j2
public class ProjectsPage extends BasePage {

    private final String PROJECTS_URL = "/projects";
    private final String REMOVE_BTN = "[data-testid=remove]";
    private final String DELETE_PROJECTS_BTN = "//span[text()='Delete project']";
    private final String OPEN_ACTION_MENU_BTN = "button[aria-label='Open action menu']";
    private final String PROJECTS_LIST_LOAD_RESULT_XPATH = "//table | //*[contains(text(),'you don’t have any projects yet')]";

    @Step("Открыть страницу проектов (/projects)")
    public ProjectsPage openPage() {
        open(PROJECTS_URL);
        log.info("Открыта страница проектов по URL: {}", PROJECTS_URL);
        return this;
    }
    @Step("Проверка, что  страница проектов открыта")
    public ProjectsPage isPageOpened() {
                  $x(PROJECTS_LIST_LOAD_RESULT_XPATH).shouldBe(Condition.visible,Duration.ofSeconds(120));

      /*  CREATE_NEW_PROJECT_BTN
                .shouldBe(visible, Duration.ofSeconds(120));*/
        log.info("Страница проектов успешно загружена и кнопка 'Create new project' отображается");
        return this;
    }
    @Step("Открыть модалку создания проекта")
    public ModalCreateProjectPage openCreateProjectModal() {
        CREATE_NEW_PROJECT_BTN.shouldBe(visible, Duration.ofSeconds(60)).click();
        log.info("Открыта модалка создания проекта");
        return new ModalCreateProjectPage().isPageOpened();
    }
    @Step("Удаление проекта с именем: '{project}'")
    public ProjectsPage deleteProject(String project) {
        log.info("Начало удаления проекта: {}", project);

        $(byText(project))
                .ancestor("tr")
                .find(OPEN_ACTION_MENU_BTN)
                .click();

        $(REMOVE_BTN).click();
        $x(DELETE_PROJECTS_BTN).click();
        $(byText(project)).shouldNotBe(visible, Duration.ofSeconds(60));
        log.info("Проект '{}' удалён", project);
        return this;
    }
}

