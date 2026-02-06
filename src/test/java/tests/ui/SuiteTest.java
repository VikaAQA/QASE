package tests.ui;

import com.codeborne.selenide.Selenide;
import dto.QaseTestSuiteDto;
import org.testng.annotations.Test;
import tests.BaseTest;
import utils.factories.api.ProjectRequestFactory;
import utils.factories.ui.TestSuiteFactory;

public class SuiteTest extends BaseTest {
/*@Test
    public void createTestSuite(){
        ProjectAPI projectAPI = new ProjectAPI();

        QaseTestSuiteDto testSuite = new TestSuiteFactory().random();
        loginAndOpenProductsPage();
        String code = projectAPI.createProjectAndReturnCode(ProjectRequestFactory.valid());
        Selenide.refresh();
        repositoryPage
                .openRepository(code)
                .openSuitPage();
        suitePage
                .isPageOpened()
                .fillFormSuitePge(testSuite);
        repositoryPage
                .shouldHaveSuite((testSuite.getSuit_name()));
    }*/
@Test
public void createTestSuite() {
    QaseTestSuiteDto testSuite = new TestSuiteFactory().random();

    loginAndOpenProductsPage();

    String code = projectAPI.createProjectAndReturnCode(ProjectRequestFactory.valid());
    Selenide.refresh();

    repositoryPage.openRepository(code);

    uiSteps.createSuite(testSuite)
            .assertSuiteVisible(testSuite.getSuit_name());
}
}

