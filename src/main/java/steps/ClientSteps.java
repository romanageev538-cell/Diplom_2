package steps;

import data.ClientData;
import data.LoginData;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.ClientModel;

import static io.restassured.RestAssured.given;

public class ClientSteps {

    @Step("Создание пользователя")
    public Response createClient(ClientModel client) {
        return given()
                .baseUri(ClientData.BASE_URL)
                .header("Content-Type", "application/json")
                .body(client)
                .when()
                .post(LoginData.REGISTER_PATH);
    }

    @Step("Удаление пользователя")
    public Response deleteClient(String accessToken) {
        return given()
                .baseUri(ClientData.BASE_URL)
                .header("Authorization", accessToken)
                .when()
                .delete(LoginData.USER_PATH);
    }
}