package adapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.project.create.CreateProjectRq;
import models.project.create.CreateProjectRs;
import models.project.get.GetProjectRs;

import java.util.List;

public class ProjectAPI extends BaseAPI{

    public static CreateProjectRs createProject(CreateProjectRq project) {
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

    public static void deleteProject(String... codes) {
        for (String code : codes) {
            spec()
                    .when()
                    .delete("https://api.qase.io/v1/project/" + code)
                    .then()
                    .log().all()
                    .statusCode(200);
        }
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

    public static List<String> getAllProject(){//извлечение из респонса коллекции и передача в другой метод
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
    public static ProjectAPI deleteAllProject() {
        getAllProject().forEach(ProjectAPI::deleteProject);
        return new ProjectAPI ();
    }



}

