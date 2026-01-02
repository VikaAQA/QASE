package adapters;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CaseAPI extends BaseAPI {

    @Step("Добавление тест-кейса в проект {project}")
    public models.create.CreateCaseResponseDTO addCase(String project, models.create.CreateCaseRequestDTO rq) {
        Response response = post("case/" + project, rq);
        return gson.fromJson(response.asString(), models.create.CreateCaseResponseDTO.class);
    }
}
