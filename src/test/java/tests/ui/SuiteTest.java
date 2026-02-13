package tests.ui;

import dto.QaseTestSuiteDto;
import org.testng.annotations.Test;
import tests.BaseTest;
import utils.factories.api.ProjectRequestFactory;
import utils.factories.ui.TestSuiteFactory;

public class SuiteTest extends BaseTest {
@Test
public void createTestSuite() {
    QaseTestSuiteDto testSuite = new TestSuiteFactory().random();
    loginAndOpenProductsPage();

    String code = projectAPI.createProjectAndReturnCode(ProjectRequestFactory.valid());

    repositoryPage.openRepository(code)
                  .isPageOpened();
    uiSteps.createSuite(testSuite)
            .assertSuiteVisible(testSuite.getSuit_name());}
}

