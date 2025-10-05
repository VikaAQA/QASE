package tests.ui;

import dto.TestCase;
import io.qameta.allure.Description;
import org.testng.annotations.Test;
import utils.CaseRequestFactory;

import static data.Elements.NAME_PROJECT;

public class CaseTest extends BaseTest {

    @Test(groups = "smoke")
    @Description("Создание тест-кейса")
    public void checkCreateCase() {
        TestCase uiCase = CaseRequestFactory.ui();
        loginPage.openPage()
                .login(user, password);
        productsPage.waittingOpen();
        modalCreateProjectPage.createProject(NAME_PROJECT);
        casePage.openCreateCase(uiCase);
        projectPage.openRepository(NAME_PROJECT);
        casePage.checkThatTestCaseIsCreated();
    }
}



