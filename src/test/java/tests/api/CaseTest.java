package tests.api;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import models.testcase.get.GetCaseErrorResponseDto;
import models.testcase.get.GetCaseResponseDto;
import models.testcase.get.GetCasesResponseDto;
import models.project.get.GetProjectResponseDto;
import org.testng.annotations.Test;
import tests.BaseTest;
import utils.factories.api.CaseRequestFactory;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Epic("API Tests")
public class CaseTest extends BaseTest {

    private static final int SMOKE_TYPE_CASE = 2;
    private static final int REGRESSION_TYPE_CASE = 3;

    @Test(groups = "smoke", description = "Создание тест-кейса и получение его по id")
    @Description("Проверка позитивного сценария добавления тест-кейса в проект.")
    public void shouldCreateTestCaseInProject() {
            String projectCode = projectAPI.createProject();
            models.create.CreateCaseRequestDto request = CaseRequestFactory.valid();
            caseAPI.assertCaseCreatedCorrectly(projectCode, request);
        }

    @Test(description = "Удаление тест-кейса")
    public void shouldDeleteTestCase(){
        String projectCode = projectAPI.createProject();
        int createdCaseId = caseAPI.createTestCaseAndReturnId(projectCode, CaseRequestFactory.valid());

        caseAPI.deleteCase(projectCode,createdCaseId);
        GetCaseErrorResponseDto getDeletedCase =  caseAPI.getCaseExpectError(projectCode,createdCaseId);
        assertThat(getDeletedCase.getStatus())
                .as("Статус ответа при получении тест-кейса должен быть false" )
                .isFalse();
    }
    @Test(description = "Обновление тест-кейса: PATCH, поле type")
    @Description("Проверка корректного обновления тест-кейса методом PATCH на примере поля type.")
    public void shouldUpdateTestCase() {
        String projectCode = projectAPI.createProject();
        int createdCaseId = caseAPI.createTestCaseAndReturnId(projectCode, CaseRequestFactory.valid());

        caseAPI.updateCaseType(projectCode, createdCaseId, SMOKE_TYPE_CASE);
        GetCaseResponseDto updatedCase = caseAPI.getCase(projectCode, createdCaseId);

        assertThat(updatedCase.getResult().getType())
                .as("Значение поля type должно обновиться")
                .isEqualTo(SMOKE_TYPE_CASE);
    }

    @Test(description = "Добавление нескольких тест-кейсов и проверка их количества в проекте")
    @Description("Проверка корректного увеличения количества тест-кейсов в проекте.")
    public void shouldIncreaseTestCaseCountInProject() {
        String projectCode = projectAPI.createProject();
        caseAPI.createSeveralTestCases(projectCode, 4);

      GetProjectResponseDto project = projectAPI.getProjectByCode(projectCode);
        assertThat(project.getResult().getCounts().getCases())
                .as("Количество тест-кейсов в проекте должно увеличиться на добавленное значение")
                .isEqualTo(4);
    }

    @Test(description = "Фильтрация тест-кейсов по типу ")
    @Description("""
            Проверка корректной фильтрации тест-кейсов:
            - создаётся проект
            - добавляются тест-кейсы с разными типами
            - выполняется фильтрация по типу SMOKE
            - проверяется количество отфильтрованных тест-кейсов
            """)
    public void shouldFilterCasesByType() {
        String projectCode = projectAPI.createProject();
        caseAPI.createSeveralTestCases(projectCode, 8, SMOKE_TYPE_CASE);
        caseAPI.createSeveralTestCases(projectCode, 12, REGRESSION_TYPE_CASE);

        GetCasesResponseDto allCases = caseAPI.getAllCases(projectCode);

        List<GetCasesResponseDto.CaseItem> smokeCases =
                caseAPI.filterCasesByType(allCases, SMOKE_TYPE_CASE);

        assertThat(smokeCases.size())
                .as("После фильтрации по типу SMOKE должно остаться 8 тест-кейсов")
                .isEqualTo(8);
    }
}







