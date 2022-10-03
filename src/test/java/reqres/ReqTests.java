package reqres;

import io.restassured.http.ContentType;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqTests {

    private static final int STATUS_CODE_200 = 200;
    private static final int STATUS_CODE_404 = 404;
    private static final int STATUS_CODE_204 = 204;
    private static final int STATUS_CODE_400 = 400;

    @Test
    @DisplayName("Тест регистрация")
    void registrationTest() {
        final String uri = "https://reqres.in/api/register";

        JSONObject requestParams = new JSONObject();
        requestParams.put("email", "eve.holt@reqres.in");
        requestParams.put("password", "pistol");

        JSONObject expectedBody = new JSONObject();
        expectedBody.put("id", 4);
        expectedBody.put("token", "QpwL5tke4Pnpja7X4");

        String actualBody = given()
                .log().uri()
                .contentType(ContentType.JSON)
                .body(requestParams.toString())
                .when()
                .post(uri)
                .then()
                .log().status()
                .log().body()
                .statusCode(STATUS_CODE_200)
                .extract().response().getBody().asString();

        assertEquals(expectedBody.toString(), actualBody);
    }

    @Test
    @DisplayName("Тест на успешное получение имени и фамилии")
    void singleUserFirstNameLastNameTest() {

        final String uri = "https://reqres.in/api/users/7";

        String expectedFirstName = "Michael";
        String expectedLastName = "Lawson";

        given()
                .log().uri()
                .when()
                .get(uri)
                .then()
                .log().status()
                .log().body()
                .statusCode(STATUS_CODE_200)
                .assertThat()
                .body("data.first_name", is(expectedFirstName))
                .body("data.last_name", is(expectedLastName));

    }

    @Test
    @DisplayName("Тест на ошибку")
    void errorTest() {

        final String uri = "https://reqres.in/api/unknown/23";

        given()
                .log().uri()
                .when()
                .get(uri)
                .then()
                .log().status()
                .statusCode(STATUS_CODE_404);

    }

    @Test
    @DisplayName("Тест на успешное удаление юзера")
    void deletedTest() {

        final String uri = "https://reqres.in/api/users/2";

        given()
                .log().uri()
                .when()
                .delete(uri)
                .then()
                .log().status()
                .statusCode(STATUS_CODE_204);
    }

    @Test
    @DisplayName("Тест на ошибку в почте")
    void missingPasswordTest() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("email", "peter@klaven");

        final String uri = "https://reqres.in/api/login";

            given()
                .log().uri()
                .contentType(ContentType.JSON)
                .body(requestParams)
                .when()
                .post(uri)
                .then()
                .log().status()
                .log().body()
                .statusCode(STATUS_CODE_400)
                .extract().response().getBody().asString();
    }

}
