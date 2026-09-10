package steps;

import data.ClientData;
import data.LoginData;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.LoginModel;

import static io.restassured.RestAssured.given;

public class LoginSteps {

    @Step("Логин пользователя")
    public Response login(LoginModel login) {
        return given()
                .baseUri(ClientData.BASE_URL)
                .header("Content-Type", "application/json")
                .body(login)
                .when()
                .post(LoginData.LOGIN_PATH);
    }
}