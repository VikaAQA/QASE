package adapters;

import io.restassured.http.ContentType;
import io.restassured.http.Cookies;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class AuthAPI {

    private static final String BASE_URL = "https://app.qase.io";

    public Cookies login(String user, String password) {

        return given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "email", user,
                        "password", password,
                        "remember", true
                ))
                .when()
                .post("/v1/auth/login/regular")
                .then()
                .statusCode(204)
                .extract()
                .response()
                .getDetailedCookies();
    }
}
