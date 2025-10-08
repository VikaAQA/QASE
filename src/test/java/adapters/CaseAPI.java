package adapters;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;
import models.create.CreateCaseRq;
import models.create.CreateCaseRs;

@Log4j2
public class CaseAPI extends BaseAPI {

    @Step("Добавление тест-кейса в проект {project}")
    public CreateCaseRs addCase(String project, CreateCaseRq rq) {
        Response response = post("case/" + project, rq);
        return gson.fromJson(response.asString(), CreateCaseRs.class);
    }
}
