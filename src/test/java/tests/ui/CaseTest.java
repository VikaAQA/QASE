package tests.ui;

import dto.QaseTestCaseDto;
import tests.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.factories.ui.TestCaseFactory;

import static data.Elements.NAME_PROJECT;


@Epic("UI Tests")
public class CaseTest extends BaseTest {

    @Test(groups = "smoke", description = "Создание тест-кейса через UI")
    @Description("Проверка, что пользователь может создать тест-кейс через веб-интерфейс")
    public void checkCreateCase() {
        QaseTestCaseDto uiCase = TestCaseFactory.valid();
        loginAndOpenProductsPage();
        modalCreateProjectPage.createProject(NAME_PROJECT);
        casePage.creattingTestCase(uiCase)
                .checkThatTestCaseIsCreated(1);
    }

    @DataProvider
    public Object[][] testCaseSpecs() {
        return new Object[][]{
                {
                        QaseTestCaseDto.builder().
                                title("Test Case 1").
                                status("Draft").
                               description("Description").
                                severity("Blocker").
                                priority("High").
                                type("Functional").
                                isFlaky(true).
                                behavior("Positive").
                                automationStatus("Manual").
                                build()
                },
                {
                        QaseTestCaseDto.builder().
                                title("Test Case 2").
                                status("Actual").
                               description("Description").
                                severity("Major").
                                priority("Low").
                                type("Smoke").
                                behavior("Negative").
                                automationStatus("Automated").
                                build()
                },
                {
                        QaseTestCaseDto.builder().
                                title("Test Case 3").
                                description("Description").
                                status("Deprecated").
                                severity("Trivial").
                                priority("Low").
                                type("Smoke").
                                behavior("Destructive").
                                automationStatus("Manual").
                                build()
                }
        };
    }
    @Test(description = "Создание тест-кейса с разными входными данными" , dataProvider = "testCaseSpecs")
       public void testCaseShouldHasCorrectSpecs(QaseTestCaseDto testCase) {
        loginAndOpenProductsPage();
        modalCreateProjectPage.createProject(NAME_PROJECT);
        casePage.creattingTestCase(testCase)
                .openTestCase(NAME_PROJECT, 1)
                        .isPageOpened();
        casePage.checkTestCaseSpecs(testCase);
    }
}



