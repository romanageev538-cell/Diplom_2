import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.ClientModel;
import org.junit.Test;
import io.restassured.response.Response;

import static data.ClientData.DEFAULT_PASSWORD;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class CreateUserTest extends BaseApiTest {

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка успешного создания нового пользователя с корректными данными")
    public void testCreateUniqueUser() {
        Response response = createUser();

        response.then()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("user.email", equalTo(email))
                .body("user.name", equalTo(name))
                .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Проверка ошибки при попытке создать пользователя с уже существующими учётными данными")
    public void testCreateExistingUser() {
        createUser(); // создаём первого

        // повторяем с теми же данными
        ClientModel client = new ClientModel(email, password, name);
        Response response = clientSteps.createClient(client);
        response.then()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без одного обязательного поля (без email)")
    @Description("Проверка ошибки при отсутствии email в запросе")
    public void testCreateUserWithoutEmail() {
        // используем null для email
        ClientModel client = new ClientModel(null, DEFAULT_PASSWORD, "AnyName");
        Response response = clientSteps.createClient(client);
        response.then()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}