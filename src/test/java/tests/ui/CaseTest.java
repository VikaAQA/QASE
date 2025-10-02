package tests.ui;

import io.qameta.allure.Description;
import org.testng.annotations.Test;

import static data.Elements.NAME_PROJECT;


public class CaseTest extends BaseTest {

    @Test
    @Description("Создание тест-кейса")
    public void checkCreateCase() {
        loginPage.openPage()
                .login(user, password);
        productsPage.waittingOpen();
        modalCreateProjectPage.createProject(NAME_PROJECT);
        casePage.openCreateCase();
        casePage.checkThatTestCaseIsCreated();
    }
}



