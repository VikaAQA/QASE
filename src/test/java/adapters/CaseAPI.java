package adapters;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;
import utils.factories.api.CaseRequestFactory;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@Log4j2
public class CaseAPI extends BaseAPI {

    private static final String CREATE_CASE_SCHEMA = "schema/create_case_rs.json";

    @Step("POST /case/{project} (raw)")
    public Response createCaseRaw(String project, models.create.CreateCaseRequestDTO rq) {
        return post("case/" + project, rq);
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

        assertThat(rs.getResult().getId())
                .as("Create case id должен быть не null, project=%s", project)
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


}
