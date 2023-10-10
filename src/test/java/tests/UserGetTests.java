package tests;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.qameta.allure.SeverityLevel.*;

@Epic("User tests")
@Feature("Essential features")
@Story("Get user by ID")
public class UserGetTests extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("This test gets user without authorization")
    @DisplayName("Test get user by id without auth")
    @Severity(CRITICAL)
    @Owner("Nurshat Kalimullin")
    @Link(name = "Documentation", url = "https://playground.learnqa.ru/api/map")
    public void testGetUserNotAuth(){
        Response responseUserData = RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

        Assertions.assertJsonHasKey(responseUserData, "username");
        Assertions.assertJsonDoesNotHaveKey(responseUserData, "firstName");
        Assertions.assertJsonDoesNotHaveKey(responseUserData, "lastName");
        Assertions.assertJsonDoesNotHaveKey(responseUserData, "email");

    }


    @Test
    @Description("This test gets user with correct authorization")
    @DisplayName("Test get user by id with auth")
    @Severity(BLOCKER)
    @Owner("Nurshat Kalimullin")
    @Link(name = "Documentation", url = "https://playground.learnqa.ru/api/map")
    public void testGetUserDetailsAuthAsSameUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String header = this.getHeader(responseGetAuth, "x-csrf-token");

        Response responseUserData = apiCoreRequests
                .makeGetRequestById("https://playground.learnqa.ru/api/user/", 2, header, cookie);

        String[] expectedFields = {"username", "firstName", "lastName", "email"};
        Assertions.assertJsonHasKeys(responseUserData, expectedFields);
    }


    @Test
    @Description("This test gets user with incorrect authorization")
    @DisplayName("Test get user by id with another user auth")
    @Severity(NORMAL)
    @Owner("Nurshat Kalimullin")
    @Link(name = "Documentation", url = "https://playground.learnqa.ru/api/map")
    public void testGetUserDetailsAuthAsAnotherUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String header = this.getHeader(responseGetAuth, "x-csrf-token");


        Response responseUserData = apiCoreRequests
                .makeGetRequestById("https://playground.learnqa.ru/api/user/", 1, header, cookie);

        Assertions.assertJsonHasKey(responseUserData, "username");
        Assertions.assertJsonDoesNotHaveKey(responseUserData, "firstName");
        Assertions.assertJsonDoesNotHaveKey(responseUserData, "lastName");
        Assertions.assertJsonDoesNotHaveKey(responseUserData, "email");
    }
}
