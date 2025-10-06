package tests.api;

import io.qameta.allure.Step;
import org.testng.annotations.Test;
import utils.CaseRequestFactory;
import utils.ProjectRequestFactory;

import static adapters.CaseAPI.addCase;
import static adapters.ProjectAPI.createProject;

public class CaseTest extends BaseApiTest {

    @Test(groups = "smoke")
    @Step("Добавление тест-кейса к проекту")
    public void addCaseToProject() {
        createProject(ProjectRequestFactory.validProject());
        String code = ProjectRequestFactory.validProject().getCode();
        addCase(code, CaseRequestFactory.caseRq());
    }
}

