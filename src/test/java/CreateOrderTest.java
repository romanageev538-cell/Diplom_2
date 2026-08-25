import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.OrderModel;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static data.OrderData.*;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class CreateOrderTest extends BaseApiTest {

    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    @Description("Проверка успешного создания заказа авторизованным пользователем с корректными ингредиентами")
    public void testCreateOrderWithAuthAndIngredients() {
        createUser();

        OrderModel order = new OrderModel(Arrays.asList(
                VALID_INGREDIENT_FIRST,
                VALID_INGREDIENT_SECOND,
                VALID_INGREDIENT_THIRD,
                VALID_INGREDIENT_FOURTH
        ));
        Response response = orderSteps.createOrderWithAuth(order, accessToken);
        response.then()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("order.owner.email", equalTo(email))
                .body("order.owner.name", equalTo(name))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка создания заказа без токена – заказ всё равно создаётся, возвращается только номер")
    public void testCreateOrderWithoutAuth() {
        OrderModel order = new OrderModel(Arrays.asList(
                VALID_INGREDIENT_FIRST,
                VALID_INGREDIENT_SECOND,
                VALID_INGREDIENT_THIRD,
                VALID_INGREDIENT_FOURTH
        ));
        Response response = orderSteps.createOrderWithoutAuth(order);
        response.then()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов (с авторизацией)")
    @Description("Проверка ошибки 400 при попытке создать заказ с пустым списком ингредиентов")
    public void testCreateOrderWithoutIngredients() {
        createUser();

        OrderModel order = new OrderModel(Collections.emptyList());
        Response response = orderSteps.createOrderWithAuth(order, accessToken);
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("success", is(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов(с авторизацией)")
    @Description("Проверка ошибки 500 при передаче несуществующих ID ингредиентов")
    public void testCreateOrderWithInvalidHash() {
        createUser();

        OrderModel order = new OrderModel(Arrays.asList(INVALID_HASH, "anotherInvalid"));
        Response response = orderSteps.createOrderWithAuth(order, accessToken);
        response.then()
                .statusCode(SC_INTERNAL_SERVER_ERROR);  // сервер возвращает 500, тело не проверяем
    }
}