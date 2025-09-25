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
import tests.PropertyReader;

import static io.restassured.RestAssured.given;

public class ProjectAPI {
    public static Gson gson = new GsonBuilder()//настраивает поведение перед созданием обьекта
            .excludeFieldsWithoutExposeAnnotation()//для сериализации(обьект в toJson) и десериализации(fromJson в обьект java),нужные поля должны быть помечены аннотацией Expose в models
            .create();

    //private static final String token = "16a159bbf58faf5e4dbdcca7339ac92571e4f8c05d3973f7f09be2856ba5761d";
    static String token = System.getProperty("token", PropertyReader.getProperty("token"));
    public static RequestSpecification spec() {
        return given()
                .contentType(ContentType.JSON)
                .header("Token", token)
                .log().all();
    }

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

