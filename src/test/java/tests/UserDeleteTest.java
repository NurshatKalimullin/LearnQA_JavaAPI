package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.qameta.allure.SeverityLevel.CRITICAL;
import static io.qameta.allure.SeverityLevel.NORMAL;

@Epic("User tests")
@Feature("Non-essential features")
@Story("Delete user")
public class UserDeleteTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();


    @Test
    @Description("This test tries to delete not-deletable user")
    @DisplayName("Test delete a base user")
    @Severity(NORMAL)
    @Owner("Nurshat Kalimullin")
    @Link(name = "Documentation", url = "https://playground.learnqa.ru/api/map")
    public void testDeleteBaseUser() {
        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //DELETE
        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/",
                        2,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid")

                );
        Assertions.assertResponseTextEquals(
                responseDeleteUser,
                "Please, do not delete test users with ID 1, 2, 3, 4 or 5."
        );


        //GET
        Response responseUserData = apiCoreRequests
                .makeGetRequestById(
                        "https://playground.learnqa.ru/api/user/",
                        2,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid")
                );

        Assertions.assertJsonByName(responseUserData, "email", "vinkotov@example.com");
    }


    @Test
    @Description("This test deletes a usual user")
    @DisplayName("Test delete a normal user")
    @Severity(NORMAL)
    @Owner("Nurshat Kalimullin")
    @Link(name = "Documentation", url = "https://playground.learnqa.ru/api/map")
    public void testDeleteUser() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateUser = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        int userId = Integer.parseInt(responseCreateUser.jsonPath().getString("id"));

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //DELETE
        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/",
                        userId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid")

                );

        //GET
        Response responseUserData = apiCoreRequests
                .makeGetRequestById(
                        "https://playground.learnqa.ru/api/user/",
                        userId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid")
                );
        Assertions.assertResponseTextEquals(responseUserData, "User not found");
    }


    @Test
    @Description("This test checks that one user is not able to delete another")
    @DisplayName("Test delete a by another user")
    @Severity(CRITICAL)
    @Owner("Nurshat Kalimullin")
    @Link(name = "Documentation", url = "https://playground.learnqa.ru/api/map")
    public void testDeleteUserByAnotherUser() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateUser = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        int userId = Integer.parseInt(responseCreateUser.jsonPath().getString("id"));

        //GENERATE SECOND USER
        Map<String, String> secondUserData = DataGenerator.getRegistrationData();

        Response responseCreateSecondUser = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", secondUserData);


        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", secondUserData.get("email"));
        authData.put("password", secondUserData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //DELETE
        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/",
                        userId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid")

                );

        //GET
        Response responseUserData = apiCoreRequests
                .makeGetRequestById(
                        "https://playground.learnqa.ru/api/user/",
                        userId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid")
                );
        Assertions.assertJsonByName(responseUserData, "username", userData.get("username"));
    }
}
