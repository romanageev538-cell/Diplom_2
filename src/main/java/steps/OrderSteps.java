package steps;

import data.ClientData;
import data.OrderData;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.OrderModel;

import static io.restassured.RestAssured.given;

public class OrderSteps {

    @Step("Создание заказа с авторизацией")
    public Response createOrderWithAuth(OrderModel order, String accessToken) {
        return given()
                .baseUri(ClientData.BASE_URL)
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(OrderData.ORDERS_PATH);
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuth(OrderModel order) {
        return given()
                .baseUri(ClientData.BASE_URL)
                .header("Content-Type", "application/json")
                .body(order)
                .when()
                .post(OrderData.ORDERS_PATH);
    }
}