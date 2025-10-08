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
                .post(BASE_URL + endpoint)
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
                .get(BASE_URL + endpoint)
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
                .delete(BASE_URL + endpoint)
                .then()
                .log().all()
                .extract()
                .response();
    }

    /** Преобразование ответа в JsonPath */
    protected JsonPath json(Response response) {
        return new JsonPath(response.asString());
    }
}
