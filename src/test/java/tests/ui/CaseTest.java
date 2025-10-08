package tests.ui;

import dto.TestCase;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.testng.annotations.Test;
import tests.BaseTest;
import utils.CaseRequestFactory;

import static data.Elements.NAME_PROJECT;
@Epic("UI Tests")
public class CaseTest extends BaseTest {

    @Test(groups = "smoke", description = "Создание тест-кейса через UI")
    @Description("Проверка, что пользователь может создать тест-кейс через веб-интерфейс")
    public void checkCreateCase() {
        TestCase uiCase = CaseRequestFactory.ui();
        loginAndOpenProductsPage();
        modalCreateProjectPage.createProject(NAME_PROJECT);
        casePage.openCreateCase(uiCase);
        projectPage.openRepository(NAME_PROJECT);
        casePage.checkThatTestCaseIsCreated();
    }
}



