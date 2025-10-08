package tests.ui;

import dto.TestCase;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.testng.annotations.DataProvider;
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
        casePage.creattingTestCase(uiCase);
        projectPage.openRepository(NAME_PROJECT);
        casePage.checkThatTestCaseIsCreated(1);
    }

    @DataProvider
    public Object[][] testCaseSpecs() {
        return new Object[][]{
                {
                        TestCase.builder().
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
                        TestCase.builder().
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
                        TestCase.builder().
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
    @Test( description = "Создание тест-кейса с разными входными данными", dataProvider = "testCaseSpecs")
    public void testCaseShouldHasCorrectSpecs(TestCase testCase) {
        loginAndOpenProductsPage();
        modalCreateProjectPage.createProject(NAME_PROJECT);
        casePage.creattingTestCase(testCase)
                .openTestCase(NAME_PROJECT,1);
        casePage.checkTestCaseSpecs(testCase);
    }
}



