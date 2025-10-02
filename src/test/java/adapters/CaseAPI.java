package adapters;

import io.restassured.module.jsv.JsonSchemaValidator;
import models.create.CreateCaseRq;
import models.create.CreateCaseRs;
import models.project.create.CreateProjectRq;
import models.project.create.CreateProjectRs;

public class CaseAPI extends BaseAPI {

    public static CreateCaseRs addCase(String project, CreateCaseRq rq) {
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
