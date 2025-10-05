package adapters;

import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import models.create.CreateCaseRq;
import models.create.CreateCaseRs;

@Log4j2
public class CaseAPI extends BaseAPI {

    @Step
    public static CreateCaseRs addCase(String project, CreateCaseRq rq) {
        log.info("Sending POST request to /case/{} with body: {}", project, rq);
        return spec()
                .contentType("application/json")
                .body(gson.toJson(rq)) // <-- отправляем правильное тело
                .when()
                .post("https://api.qase.io/v1/case/" + project) // <-- без пробела
                .then()
                .log().all()
                .extract()
                .as(CreateCaseRs.class);
    }
}
