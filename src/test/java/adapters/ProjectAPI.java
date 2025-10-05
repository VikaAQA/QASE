package adapters;

import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;
import models.project.create.CreateProjectRq;
import models.project.create.CreateProjectRs;
import models.project.get.GetProjectRs;

import java.util.List;

@Log4j2
public class ProjectAPI extends BaseAPI {
    @Step
    public static CreateProjectRs createProject(CreateProjectRq project) {
        log.info("Creating project with body: {}", project);
        return spec()
                .body(gson.toJson(project)) //преобразуем обьект в json
                .when()//КОГДА
                .post("https://api.qase.io/v1/project")
                .then()//тогда
                //.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/create_project_rs.json"))//валидация json по структуре и типу данных
                .log().all()
                .statusCode(200)
                .extract()
                .as(CreateProjectRs.class);// // Преобразуем JSON в Java объект
    }

    @Step
    public static void deleteProject(String... codes) {
        for (String code : codes) {
            log.info("Deleting project with code: {}", code);
            spec()
                    .when()
                    .delete("https://api.qase.io/v1/project/" + code)
                    .then()
                    .log().all()
                    .statusCode(200);
        }
    }

    @Step
    public static GetProjectRs getProject(String code) {
        log.info("Getting project with code: {}", code);
        return spec()
                .when()//КОГДА
                .get("https://api.qase.io/v1/project/" + code)
                .then()//тогда
                .log().all()
                .statusCode(200)
                .extract()//извлекаем респонс
                .as(GetProjectRs.class); // Преобразуем JSON в Java объект
    }

    @Step
    public static Response createProjectWithValidation(CreateProjectRq project) {
        log.info("Creating project with validation, body: {}", project);
        return spec()
                .body(project)
                .when()
                .post("https://api.qase.io/v1/project")
                .then()
                .log().all() // Логируем для отладки
                .extract()
                .response();
    }

    @Step
    public static List<String> getAllProject() {//извлечение из респонса коллекции и передача в другой метод
        log.info("Retrieving all projects");
        String response = spec()
                .when()
                .get("https://api.qase.io/v1/project?limit=10&offset=0")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .asString();
        JsonPath json = new JsonPath(response);

        return json.getList("result.entities.code");
    }

    @Step
    public static ProjectAPI deleteAllProject() {
        log.info("Deleting all projects");
        getAllProject().forEach(ProjectAPI::deleteProject);
        return new ProjectAPI();
    }
}

