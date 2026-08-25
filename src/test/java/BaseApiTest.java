import data.ClientData;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.ClientModel;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import steps.ClientSteps;
import steps.LoginSteps;
import steps.OrderSteps;

import static org.apache.http.HttpStatus.*;


public class BaseApiTest {

    protected ClientSteps clientSteps;
    protected LoginSteps loginSteps;
    protected OrderSteps orderSteps;
    protected String accessToken;
    protected String email;
    protected String password;
    protected String name;

    @Before
    public void setUp() {
        RestAssured.baseURI = ClientData.BASE_URL;
        RestAssured.filters(new AllureRestAssured());
        clientSteps = new ClientSteps();
        loginSteps = new LoginSteps();
        orderSteps = new OrderSteps();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            clientSteps.deleteClient(accessToken)
                    .then().statusCode(SC_ACCEPTED);
        }
    }

    protected Response createUser() {
        ClientModel client = ClientData.createDefaultClient();
        Response response = clientSteps.createClient(client);
        response.then().statusCode(SC_OK);
        this.accessToken = response.path("accessToken");
        this.email = client.getEmail();
        this.password = client.getPassword();
        this.name = client.getName();
        return response;
    }
}