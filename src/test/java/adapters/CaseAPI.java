package adapters;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;
import models.BaseSuccessResponse;
import models.testcase.create.CreateCaseResponseDto;
import models.testcase.get.GetCaseErrorResponseDto;
import models.testcase.get.GetCaseResponseDto;
import models.testcase.get.GetCasesResponseDto;
import models.testcase.update.UpdateCaseRequestDto;
import models.testcase.update.UpdateCaseResponseDto;
import utils.factories.api.CaseRequestFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Log4j2
public class CaseAPI extends BaseAPI {

    private static final String CREATE_CASE_SCHEMA = "schema/create_case_rs.json";

    @Step("POST /case/{project} (raw)")
    public Response createCaseRaw(String project, models.create.CreateCaseRequestDto rq) {
        return post("case/" + project, rq);
    }

    @Step("GET /case/{project}/{caseId} (raw)")
    public Response getCaseRaw(String project, int caseId) {
        return get("case/" + project + "/" + caseId);
    }

    @Step("GET /case/{project} (raw)")
    public Response getAllCasesRaw(String project) {
        return get("case/" + project );
    }

    @Step("Patch /case/{project}/{caseId} (raw)")
    public Response updateCaseRaw(String project, int caseId, UpdateCaseRequestDto body) {
        return patch("case/" + project + "/" + caseId, body);
    }

    @Step("Delete /case/{project}/{caseId} (raw)")
    public Response deleteCaseRaw (String project, int caseId){
        return delete("case/" + project + "/" + caseId);
    }

    @Step("Удалить тест-кейс {caseId} в проекте {projectCode}")
    public void deleteCase(String projectCode, int caseId) {
        Response response = deleteCaseRaw(projectCode, caseId);

        assertThat(response.statusCode())
                .as("Удаление тест-кейса: project=%s, caseId=%s, body=%s",
                        projectCode, caseId, response.asString())
                .isIn(200, 204);
    }

    private <T extends BaseSuccessResponse> T generalAssertions(Response response, String projectCode, Class<T> classOfT) {

        assertThat(response.statusCode())
                .as("HTTP-статус должен быть 200 или 201, project=%s, body=%s",
                        projectCode, response.asString())
                .isIn(200, 201);

        T responseDto = gson.fromJson(response.asString(), classOfT);

        assertThat(responseDto)
                .as("Ответ должен быть не null, project=%s", projectCode)
                .isNotNull();

        assertThat(responseDto.getStatus())
                .as("status должен быть true, project=%s", projectCode)
                .isTrue();

        assertThat(responseDto.getResult())
                .as("result должен быть не null, project=%s", projectCode)
                .isNotNull();

        return responseDto;
    }


    @Step("Создать тест-кейс в проекте {projectCode}")
    public CreateCaseResponseDto createTestCase(String projectCode, models.create.CreateCaseRequestDto request) {
        Response response = createCaseRaw(projectCode, request);

        CreateCaseResponseDto createCaseResponseDto  = generalAssertions(response, projectCode, CreateCaseResponseDto.class);
        assertThat(createCaseResponseDto.getResult().getId())
                .as("ID созданного тест-кейса должен быть заполнен, project=%s", projectCode)
                .isNotNull();

        return createCaseResponseDto;
    }

    @Step("Добавление {countCases} тест-кейсов в проект {projectCode}")
    public List<Integer> createSeveralTestCases(String projectCode, int countCases) {
        assertThat(countCases)
                .as("Количество тест-кейсов для создания должно быть больше 0")
                .isGreaterThan(0);

        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < countCases; i++) {
            CreateCaseResponseDto rs =
                    createTestCase(projectCode, CaseRequestFactory.valid());
            ids.add(rs.getResult().getId());
        }
         return ids;
    }

    @Step("Добавление {countCases} тест-кейсов типа {type} в проект {projectCode}")
    public List<Integer> createSeveralTestCases(String projectCode, int countCases, int type) {
        assertThat(countCases)
                .as("Количество тест-кейсов для создания должно быть больше 0")
                .isGreaterThan(0);

        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < countCases; i++) {
            CreateCaseResponseDto rs =
                    createTestCase(projectCode, CaseRequestFactory.validWithType(type));
            ids.add(rs.getResult().getId());
        }
        return ids;
    }

    @Step("Создать тест-кейс в проекте {projectCode} и вернуть его id")
    public int createTestCaseAndReturnId(String projectCode, models.create.CreateCaseRequestDto rq) {
        return createTestCase(projectCode, rq).getResult().getId();
    }

    @Step("Получить тест-кейс по id {caseId} в проекте {projectCode}")
    public GetCaseResponseDto getCase(String projectCode, int caseId) {
        Response response = getCaseRaw(projectCode, caseId);

        GetCaseResponseDto getCaseResponseDto = generalAssertions(response, projectCode, GetCaseResponseDto.class);
        return getCaseResponseDto;
    }
    @Step("Получить тест-кейс по id {caseId} в проекте {projectCode} ожидаемо 404")
    public GetCaseErrorResponseDto getCaseExpectError(String projectCode, int caseId) {
        Response response = getCaseRaw(projectCode, caseId);

        assertThat(response.statusCode())
                .as("HTTP-статус получения тест-кейса должен быть 404, project=%s, caseId=%s, body=%s",
                        projectCode, caseId, response.asString())
                .isEqualTo(404);

        GetCaseErrorResponseDto responseDto =
                gson.fromJson(response.asString(), GetCaseErrorResponseDto.class);

        assertThat(responseDto)
                .as("Ответ получения тест-кейса должен быть не null, project=%s, caseId=%s",
                        projectCode, caseId)
                .isNotNull();
          return responseDto;
    }
    @Step("Получить список всех тест-кейсов в проекте {projectCode}")
    public GetCasesResponseDto getAllCases(String projectCode) {
        Response response = getAllCasesRaw(projectCode);
        GetCasesResponseDto getCasesResponseDto = generalAssertions(response, projectCode, GetCasesResponseDto.class);
        return getCasesResponseDto;
    }

    @Step("Обновить поле type у тест-кейса {caseId} в проекте {projectCode} на значение {type}")
    public UpdateCaseResponseDto updateCaseType(String projectCode, int caseId, int type) {
        UpdateCaseRequestDto request = CaseRequestFactory.updateTypeCase(type);

        Response response = updateCaseRaw(projectCode, caseId, request);
        UpdateCaseResponseDto updateCaseResponseDto = generalAssertions(response, projectCode, UpdateCaseResponseDto.class);
        return updateCaseResponseDto;
   }
    @Step("Отфильтровать тест-кейсы по типу {type}")
    public List<GetCasesResponseDto.CaseItem> filterCasesByType(
            GetCasesResponseDto response,
            int type
    ) {
        return response.getResult().getEntities().stream()
                .filter(c -> Objects.equals(c.getType(), type))
                .collect(Collectors.toList());
    }
    @Step("Создать тест-кейс и проверить, что он корректно сохранён")
    public void assertCaseCreatedCorrectly(String projectCode, models.create.CreateCaseRequestDto expected) {

        int caseId = createTestCaseAndReturnId(projectCode, expected);
        GetCaseResponseDto actual = getCase(projectCode, caseId);

        assertThat(actual.getResult())
                .as("Созданный тест-кейс должен соответствовать данным запроса")
                .satisfies(result -> {
                    assertThat(result.getId())
                            .as("ID созданного тест-кейса должен быть сгенерирован")
                            .isNotNull();
                    assertThat(result.getTitle())
                            .as("Поле title должно совпадать с переданным при создании")
                            .isEqualTo(expected.getTitle());
                    assertThat(result.getDescription())
                            .as("Поле description должно совпадать с переданным при создании")
                            .isEqualTo(expected.getDescription());
                    assertThat(result.getSeverity())
                            .as("Поле severity должно совпадать с переданным при создании")
                            .isEqualTo(expected.getSeverity());
                    assertThat(result.getPriority())
                            .as("Поле priority должно совпадать с переданным при создании")
                            .isEqualTo(expected.getPriority());
         });
    }
}
