package adapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import utils.PropertyReader;

import static io.restassured.RestAssured.given;

public class BaseAPI {
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
}
