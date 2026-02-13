package tests.ui;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.testng.annotations.Test;
import tests.BaseTest;
import tests.Retry;

import static com.codeborne.selenide.Selenide.$;
import static data.Elements.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Epic("UI Tests")
public class ProjectTest extends BaseTest {
    @Test(retryAnalyzer = Retry.class, groups = "smoke", description = "Создание проекта и его удаление")
    @Description("Проверка позитивного сценария создания проекта")
    public void checkCreateProject() {
        loginAndOpenProductsPage();

        uiSteps.createProject(NAME_PROJECT)
                .deleteProject(NAME_PROJECT);
    }
    @Test(description = "Создание проекта без названия: проверка ошибки")
    public void checkCreateProjectWithNegative() {
        loginAndOpenProductsPage();

        uiSteps.createProjectWithoutTitle();

        String validationMessage = $(PROJECT_NAME_FIELD_CSS).getAttribute(VALIDATION_MESSAGE);
        assertThat(validationMessage)
                .as("Проверка сообщения о незаполненном поле")
                .matches("(?i)(Заполните это поле\\.|Please fill out this field\\.)");
    }
}

