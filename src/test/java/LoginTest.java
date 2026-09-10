import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.LoginModel;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.*;

public class LoginTest extends BaseApiTest {

    @Before
    public void prepareUser() {
        createUser(); // создаём пользователя перед каждым тестом
    }

    @Test
    @DisplayName("Вход под существующим пользователем")
    @Description("Проверка успешного логина с верными email и паролем")
    public void testLoginExistingUser() {
        LoginModel login = new LoginModel(email, password);
        Response response = loginSteps.login(login);
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true))
                .body("user.email", equalTo(email))
                .body("user.name", equalTo(name))
                .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Вход с неверным email")
    @Description("Проверка ошибки при входе с несуществующим email")
    public void testLoginInvalidEmail() {
        String wrongEmail = "wrong_" + email;
        LoginModel login = new LoginModel(wrongEmail, password);
        Response response = loginSteps.login(login);
        response.then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Вход с неверным паролем")
    @Description("Проверка ошибки при входе с неправильным паролем")
    public void testLoginInvalidPassword() {
        LoginModel login = new LoginModel(email, "wrongPassword");
        Response response = loginSteps.login(login);
        response.then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}