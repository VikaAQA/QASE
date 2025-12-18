package tests.ui;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.testng.annotations.Test;
import tests.BaseTest;
import tests.Retry;

import static com.codeborne.selenide.Selenide.$;
import static data.Elements.NAME_PROJECT;
import static data.Elements.VALIDATION_MESSAGE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static pages.ModalCreateProjectPage.PROJECT_NAME_FIELD_CSS;

@Epic("UI Tests")
public class ProjectTest extends BaseTest {

    @Test(retryAnalyzer = Retry.class, groups = "smoke", description = "Создание проекта и его удаление")
    @Description("Проверка позитивного сценария создания проекта")
    public void checkCreateProject() {
        loginAndOpenProductsPage();
        modalCreateProjectPage.createProject(NAME_PROJECT);
        projectPage.checkCreatingProject(NAME_PROJECT);
        productsPage.openPage().isPageOpened()
                    .deleteProject(NAME_PROJECT);
    }

    @Test(description = "Создание проекта без названия: проверка ошибки")
    public void checkCreateProjectWithNegative() {
        loginAndOpenProductsPage();
        modalCreateProjectPage.createFailProject();
        String validationMessage = $(PROJECT_NAME_FIELD_CSS).getAttribute(VALIDATION_MESSAGE);
        assertThat(validationMessage)
                .as("Проверка сообщения о незаполненном поле")
                .matches("(?i)(Заполните это поле\\.|Please fill out this field\\.)");
    }
}
