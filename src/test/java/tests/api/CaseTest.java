package tests.api;

import io.restassured.response.Response;
import models.GetCaseResponseDto;
import models.GetCasesResponseDto;
import models.UpdateCaseRequestDTO;
import models.project.get.GetProjectResponseDto;
import tests.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.testng.annotations.Test;
import utils.factories.api.CaseRequestFactory;
import utils.factories.api.ProjectRequestFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Epic("API Tests")
public class CaseTest extends BaseTest {

    @Test(groups = "smoke", description = "Добавление нового тест-кейса в существующий проект")
    @Description("Проверка позитивного сценария добавления тест-кейса в проект.")
      public void addCaseToProject() {
        String code = projectAPI.createProjectAndReturnCode(ProjectRequestFactory.valid());
         caseAPI.addCase(code, CaseRequestFactory.valid());
    }

    @Test(description = "Добавление несколько тест-кейсов к проекту и определение количества кейсов у проекта")
    @Description("Проверка количеств тест-кейсов у проекта")
    public void checkCountCase(){
        String code = projectAPI.createProjectAndReturnCode(ProjectRequestFactory.valid());
        caseAPI.addFewCases(code,4);
        GetProjectResponseDto rs = projectAPI.getProjectByCode(code);//rs переименовть
        assertThat(rs.getResult().getCounts().getCases()).isEqualTo(4);
   }

   @Test(description = "Обновление тест-кейса")
   @Description("Проверка метод Patch")
   public void checkUpdateCase(){
         String code = projectAPI.createProjectAndReturnCode(ProjectRequestFactory.valid());
       models.create.CreateCaseResponseDTO rs =caseAPI.addCase(code, CaseRequestFactory.valid());
       int caseId = rs.getResult().getId();
       UpdateCaseRequestDTO body = CaseRequestFactory.updateTypeCase(6);

       Response response = caseAPI.updateCaseRaw(code,caseId,body);
       assertThat(response.jsonPath().getBoolean("status")).isTrue();

      GetCaseResponseDto checkType =  caseAPI.getCase(code,caseId);
       assertThat( checkType .getResult().getType())
               .as("Поле type должно обновиться")
               .isEqualTo(6);
   }
    //создали 20 тестов, из них 12 с типом  smoke.В апи тесте получаем все тесты, фильтруем их по типу smoke (самой с помощью коллекции,привести коллекцию к стриму отфильтровать по тегу смоук , и привести к коллекции ) и проверяем что их количество 12

    @Test(description = "Фильтрация тест-кейсов по типу ")
    @Description("Проверка корректной фильтрации ")
    public void checkFilterCase(){
        String code = projectAPI.createProjectAndReturnCode(ProjectRequestFactory.valid());

        int SMOKE_TYPE_CASE=2;
        int REGRESSION_TYPE_CASE=3;
        caseAPI.addFewCases(code,8,SMOKE_TYPE_CASE);
        caseAPI.addFewCases(code,12,REGRESSION_TYPE_CASE);

        GetCasesResponseDto allCases = caseAPI.getAllCases(code);

        List<GetCasesResponseDto.CaseItem> smokeCases =
                allCases.getResult().getEntities().stream()
                        .filter(c -> Objects.equals(c.getType(), SMOKE_TYPE_CASE))
                        .collect(Collectors.toList());

        assertThat(smokeCases.size()).isEqualTo(8);
    }
}







