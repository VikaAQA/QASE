package adapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.project.create.CreateProjectRq;
import models.project.create.CreateProjectRs;
import models.project.get.GetProjectRs;
import utils.PropertyReader;

import static io.restassured.RestAssured.given;

public class ProjectAPI extends BaseAPI{


    public static CreateProjectRs createProject(CreateProjectRq project) {
        return spec()
                .body(gson.toJson(project)) //преобразуем обьект в json
                .when()//КОГДА
                .post("https://api.qase.io/v1/project")
                .then()//тогда
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/create_project_rs.json"))//валидация json по структуре и типу данных
                .log().all()
                .statusCode(200)
                .extract()
                .as(CreateProjectRs.class);// // Преобразуем JSON в Java объект
    }

    public static void deleteProject(String code) {
        spec()
                .body(code)
                .when()//КОГДА
                .delete("https://api.qase.io/v1/project/" + code)
                .then()//тогда
                .log().all()
                .statusCode(200);
    }

    public static GetProjectRs getProject(String code) {
        return spec()
                .when()//КОГДА
                .get("https://api.qase.io/v1/project/" + code)
                .then()//тогда
                .log().all()
                .statusCode(200)
                .extract()//извлекаем респонс
                .as(GetProjectRs.class); // Преобразуем JSON в Java объект
    }

    public static Response createProjectWithValidation(CreateProjectRq project) {
        return spec()
                .body(project)
                .when()
                .post("https://api.qase.io/v1/project")
                .then()
                .log().all() // Логируем для отладки
                .extract()
                .response();
    }
}

