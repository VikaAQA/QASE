package adapters;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;
import models.GetCaseResponseDto;
import models.GetCasesResponseDto;
import models.UpdateCaseRequestDTO;
import models.project.create.CreateProjectRequestDto;
import models.project.create.CreateProjectResponseDto;
import models.project.get.GetProjectResponseDto;
import utils.factories.api.CaseRequestFactory;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.testng.AssertJUnit.assertTrue;

@Log4j2
public class CaseAPI extends BaseAPI {

    private static final String CREATE_CASE_SCHEMA = "schema/create_case_rs.json";

    @Step("POST /case/{project} (raw)")
    public Response createCaseRaw(String project, models.create.CreateCaseRequestDTO rq) {
        return post("case/" + project, rq);
    }

    @Step("Patch /case/{project}/{idCase} (raw)")
    public Response updateCaseRaw(String project, int idCase, UpdateCaseRequestDTO body) {
        return patch("case/" + project + "/" + idCase, body);
    }

    @Step("GET /case/{project}/{idCase} (raw)")
    public Response getCaseRaw(String project, int idCase) {
        return get("case/" + project + "/" + idCase);
    }

    @Step("GET /case/{project} (raw)")
    public Response getAllCaseRaw(String project) {
        return get("case/" + project );
    }

    @Step("Получить данные всех кейсов: project={project}, caseId={caseId} (GET /case/{project})")
    public GetCasesResponseDto getAllCases(String project) {
        Response response = getAllCaseRaw(project);

        assertThat(response.statusCode())
                .as("Get case HTTP status, project=%s, caseId=%s, body=%s",
                        project, response.asString())
                .isEqualTo(200);

        GetCasesResponseDto rs = gson.fromJson(response.asString(), GetCasesResponseDto.class);

        assertThat(rs)
                .as("GetCaseResponseDto должен быть не null, project=%s", project)
                .isNotNull();

        assertThat(rs.getStatus())
                .as("Get case status должен быть true, project=%s", project)
                .isTrue();

        assertThat(rs.getResult())
                .as("Get case result должен быть не null, project=%s", project)
                .isNotNull();

        return rs;
    }

    @Step("Создать тест-кейс и вернуть его id")
    public int addCaseAndReturnId(String project, models.create.CreateCaseRequestDTO rq ) {
        models.create.CreateCaseResponseDTO rs = addCase(project, rq);
        Integer caseId = rs.getResult().getId();

        assertThat(caseId)
                .as("Create case id должен быть не null, project=%s", project)
                .isNotNull();

        return caseId;
    }

    @Step("Получить данные кейса: project={project}, caseId={caseId} (GET /case/{project}/{caseId})")
    public GetCaseResponseDto getCase(String project, int caseId) {
        Response response = getCaseRaw(project, caseId);

        assertThat(response.statusCode())
                .as("Get case HTTP status, project=%s, caseId=%s, body=%s",
                        project, caseId, response.asString())
                .isEqualTo(200);

        GetCaseResponseDto rs = gson.fromJson(response.asString(), GetCaseResponseDto.class);

        assertThat(rs)
                .as("GetCaseResponseDto должен быть не null, project=%s, caseId=%s", project, caseId)
                .isNotNull();

        assertThat(rs.getStatus())
                .as("Get case status должен быть true, project=%s, caseId=%s", project, caseId)
                .isTrue();

        assertThat(rs.getResult())
                .as("Get case result должен быть не null, project=%s, caseId=%s", project, caseId)
                .isNotNull();

        return rs;
    }

    @Step("Создание тест-кейса в проект {project} (POST /case/{project})")
    public models.create.CreateCaseResponseDTO addCase(
            String project,
            models.create.CreateCaseRequestDTO rq
    ) {
        Response response = createCaseRaw(project, rq);

        assertThat(response.statusCode())
                .as("Create case HTTP status, project=%s, body=%s",
                        project, response.asString())
                .isIn(200, 201);

               // validateSchema(response, CREATE_CASE_SCHEMA);

        models.create.CreateCaseResponseDTO rs =
                gson.fromJson(response.asString(), models.create.CreateCaseResponseDTO.class);

        assertThat(rs)
                .as("CreateCaseResponseDTO должен быть не null, project=%s", project)
                .isNotNull();

        assertThat(rs.getStatus())
                .as("Create case status must be true, project=%s", project)
                .isTrue();

        assertThat(rs.getResult())
                .as("Create case result должен быть не null, project=%s", project)
                .isNotNull();

        return rs;
    }

    @Step("Добавление {countCases} тест-кейсов в проект {projectCode}")
    public List<Integer> addFewCases(String projectCode, int countCases) {
        assertThat(countCases)
                .as("countCases должен быть больше 0")
                .isGreaterThan(0);

        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < countCases; i++) {
            models.create.CreateCaseResponseDTO rs =
                    addCase(projectCode, CaseRequestFactory.valid());
            ids.add(rs.getResult().getId());
        }
         return ids;
    }

    @Step("Добавление {countCases} тест-кейсов типа {type} в проект {projectCode}")
    public List<Integer> addFewCases(String projectCode, int countCases, int type) {
        assertThat(countCases)
                .as("countCases должен быть больше 0")
                .isGreaterThan(0);

        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < countCases; i++) {
            models.create.CreateCaseResponseDTO rs =
                    addCase(projectCode, CaseRequestFactory.validWithType(type));
            ids.add(rs.getResult().getId());
        }
        return ids;
    }
}
