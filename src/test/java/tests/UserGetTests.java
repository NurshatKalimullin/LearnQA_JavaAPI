package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserGetTests extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
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
