package data;

import model.ClientModel;

public class ClientData {
    public static final String BASE_URL = "https://stellarburgers.education-services.ru/";
    public static final String DEFAULT_PASSWORD = "password123";

    public static ClientModel createDefaultClient() {
        String email = "user_" + System.currentTimeMillis() + "@mail.ru";
        String name = "User_" + System.currentTimeMillis();
        return new ClientModel(email, DEFAULT_PASSWORD, name);
    }
}