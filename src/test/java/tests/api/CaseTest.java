package tests.api;

import io.restassured.response.Response;
import models.project.create.CreateProjectRequestDto;
import models.project.get.GetProjectResponseDto;
import tests.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.testng.annotations.Test;
import utils.factories.api.CaseRequestFactory;
import utils.factories.api.ProjectRequestFactory;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Epic("API Tests")
public class CaseTest extends BaseTest {

    @Test(groups = "smoke", description = "Добавление нового тест-кейса в существующий проект")
    @Description("Проверка позитивного сценария добавления тест-кейса в проект.")
      public void addCaseToProject() {
        String code = projectAPI.createProjectAndReturnCode(ProjectRequestFactory.valid());
         caseAPI.addCase(code, CaseRequestFactory.valid());
    }

    @Test(description = "Добавление несколько тест-кеейсов к проекту и определение количества кейсов у проекта")
    @Description("Проверка количеств тест-кейсов у проекта")
    public void checkCountCase(){
        String code = projectAPI.createProjectAndReturnCode(ProjectRequestFactory.valid());
        caseAPI.addFewCases(code,4);
        GetProjectResponseDto rs = projectAPI.getProjectByCode(code);//rs переименовть
        assertThat(rs.getResult().getCounts().getCases()).isEqualTo(4);
   }


}




