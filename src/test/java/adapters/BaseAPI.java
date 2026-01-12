package adapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.log4j.Log4j2;
import utils.PropertyReader;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Log4j2
public class BaseAPI {

    protected static final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();
    protected static final String BASE_URL = "https://api.qase.io/v1/";
    protected static final String TOKEN = System.getProperty("token", PropertyReader.getProperty("token"));

    /** Базовая спецификация для всех запросов */
    protected RequestSpecification spec() {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .header("Token", TOKEN)
                .filter(new AllureRestAssured())
                .log().all();
    }

    /** POST-запрос с телом */
    protected Response post(String endpoint, Object body) {
        log.info("POST → {} | Body: {}", endpoint, body);
        return spec()
                .body(gson.toJson(body))
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
    }

    /** GET-запрос */
    protected Response get(String endpoint) {
        log.info("GET → {}", endpoint);
        return spec()
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
    }

    /** PATCH-запрос */
    protected Response patch(String endpoint, Object body) {
        log.info("PATCH → {} | body={}", endpoint, body);
        return spec()
                .body(body)
                .when()
                .patch(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
    }

    /** DELETE-запрос */
    protected Response delete(String endpoint) {
        log.info("DELETE → {}", endpoint);
        return spec()
                .when()
                .delete( endpoint)
                .then()
                .log().all()
                .extract()
                .response();
    }
    /** Преобразование ответа в JsonPath */
    protected JsonPath json(Response response) {
        return new JsonPath(response.asString());
    }
    protected void validateSchema(Response response, String schemaPathInResources) {
        response.then().assertThat()
                .body(matchesJsonSchemaInClasspath(schemaPathInResources));
    }
}