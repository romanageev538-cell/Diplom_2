import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.LoginModel;
import org.apache.http.HttpStatus;
import org.junit.Test;
import io.restassured.response.Response;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class LoginTest extends BaseApiTest {

    @Test
    @DisplayName("Вход под существующим пользователем")
    @Description("Проверка успешного логина с верными email и паролем")
    public void testLoginExistingUser() {
        createUser();

        LoginModel login = new LoginModel(email, password);
        Response response = loginSteps.login(login);
        response.then()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("user.email", equalTo(email))
                .body("user.name", equalTo(name))
                .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Вход с неверным логином и паролем")
    @Description("Проверка ошибки при неверных учётных данных")
    public void testLoginInvalidCredentials() {
        createUser();

        LoginModel login = new LoginModel(email, "wrongPassword");
        Response response = loginSteps.login(login);
        response.then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}