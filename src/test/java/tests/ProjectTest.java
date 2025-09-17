package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import static data.Elements.NAME_PROJECT;

@Feature("Проект ")
@Story("Создание проекта")
public class ProjectTest extends BaseTest {

    @Test
    @Description("Проверка создания и удаления нового проекта ")
    public void checkCreateProject() {
        loginPage.openPage()
                .login(user, password);
        productsPage.waittingOpen();
        modalCreateProjectPage.createProject(NAME_PROJECT);
        projectPage.checkCreatingProject(NAME_PROJECT);
        productsPage.openPage()
                .waittingOpen()
                .deleteProject(NAME_PROJECT);
    }
}
