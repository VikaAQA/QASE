package tests.api;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.testng.annotations.Test;
import tests.BaseTest;
import utils.factories.api.CaseRequestFactory;
import utils.factories.api.ProjectRequestFactory;
@Epic("API Tests")
public class CaseTest extends BaseTest {

    @Test(groups = "smoke", description = "Добавление нового тест-кейса в существующий проект")
    @Description("Проверка позитивного сценария добавления тест-кейса в проект.")
      public void addCaseToProject() {
        projectAPI.createProject(ProjectRequestFactory.valid());
        String code = ProjectRequestFactory.valid().getCode();
        caseAPI.addCase(code, CaseRequestFactory.valid());
    }
}

