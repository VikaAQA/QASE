package tests.api;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.testng.annotations.Test;
import tests.BaseTest;
import utils.CaseRequestFactory;
import utils.ProjectRequestFactory;
@Epic("API Tests")
public class CaseTest extends BaseTest {

    @Test(groups = "smoke", description = "Добавление нового тест-кейса в существующий проект")
    @Description("Проверка позитивного сценария добавления тест-кейса в проект.")
      public void addCaseToProject() {
        projectAPI.createProject(ProjectRequestFactory.validProject());
        String code = ProjectRequestFactory.validProject().getCode();
        caseAPI.addCase(code, CaseRequestFactory.caseRq());
    }
}

