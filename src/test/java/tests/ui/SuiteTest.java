package tests.ui;

import adapters.ProjectAPI;
import com.codeborne.selenide.Selenide;
import dto.TestSuite;
import org.testng.annotations.Test;
import tests.BaseTest;
import utils.factories.api.ProjectRequestFactory;
import utils.factories.ui.TestSuiteFactory;

public class SuiteTest extends BaseTest {
@Test
    public void createTestSuite(){
        ProjectAPI projectAPI = new ProjectAPI();

        TestSuite testSuite = new TestSuiteFactory().random();
        loginAndOpenProductsPage();
        String code = projectAPI.createProjectAndReturnCode(ProjectRequestFactory.valid());
        Selenide.refresh();
        projectPage.openPage(code).isPageOpened();
        suitePage.openPage()
                .isPageOpened()
                .fillFormSuitePge(testSuite)
                .shouldHaveSuite((testSuite.getSuit_name()));
    }
}
