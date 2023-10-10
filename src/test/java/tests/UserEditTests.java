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
@Story("Update user")
public class UserEditTests extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("This test checks that user is able to update  his data")
    @DisplayName("Test update a user by the user")
    @Severity(NORMAL)
    @Owner("Nurshat Kalimullin")
    @Link(name = "Documentation", url = "https://playground.learnqa.ru/api/map")
    public void testEditJustCreatedUser(){
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

        //EDIT
        String newName = "Changed name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/",
                        userId,
                        editData,
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

        Assertions.assertJsonByName(responseUserData, "firstName", newName);
    }


    @Test
    @Description("This test checks that user is not able to update his data without authorization")
    @DisplayName("Test update a user by the user without auth")
    @Severity(NORMAL)
    @Owner("Nurshat Kalimullin")
    @Link(name = "Documentation", url = "https://playground.learnqa.ru/api/map")
    public void testEditUserWithoutAuthorization(){
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateUser = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        int userId = Integer.parseInt(responseCreateUser.jsonPath().getString("id"));

        //LOGIN
        //Pass without login


        //EDIT
        String newName = "Changed name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/", userId, editData);

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //GET
        Response responseUserData = apiCoreRequests
                .makeGetRequestById(
                        "https://playground.learnqa.ru/api/user/",
                        userId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid")
                );

        Assertions.assertJsonByName(responseUserData, "firstName", userData.get("firstName"));
    }


    @Test
    @Description("This test checks that user is not able to update other users")
    @DisplayName("Test update a user by another user")
    @Severity(CRITICAL)
    @Owner("Nurshat Kalimullin")
    @Link(name = "Documentation", url = "https://playground.learnqa.ru/api/map")
    public void testEditUserByAnotherUser(){
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateUser = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        int userId = Integer.parseInt(responseCreateUser.jsonPath().getString("id"));

        //LOGIN
        Map<String, String> authAnotherUserData = new HashMap<>();
        authAnotherUserData.put("email", "vinkotov@example.com");
        authAnotherUserData.put("password", "1234");

        Response responseGetAnotherUserAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authAnotherUserData);

        //EDIT
        String newName = "Changed name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/",
                        userId,
                        editData,
                        this.getHeader(responseGetAnotherUserAuth, "x-csrf-token"),
                        this.getCookie(responseGetAnotherUserAuth, "auth_sid")

                );

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //GET
        Response responseUserData = apiCoreRequests
                .makeGetRequestById(
                        "https://playground.learnqa.ru/api/user/",
                        userId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid")
                );

        Assertions.assertJsonByName(responseUserData, "firstName", userData.get("firstName"));
    }


    @Test
    @Description("This test checks that user is not able to change email to a new incorrect one")
    @DisplayName("Test update a user with incorrect email")
    @Severity(NORMAL)
    @Owner("Nurshat Kalimullin")
    @Link(name = "Documentation", url = "https://playground.learnqa.ru/api/map")
    public void testEditUserToIncorrectEmail(){
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

        //EDIT
        String email = "totoshkaexample.com";
        Map<String, String> editData = new HashMap<>();
        editData.put("email", email);

        Response responseEditUser = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/",
                        userId,
                        editData,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid")

                );
        Assertions.assertResponseTextEquals(responseEditUser, "Invalid email format");

        //GET
        Response responseUserData = apiCoreRequests
                .makeGetRequestById(
                        "https://playground.learnqa.ru/api/user/",
                        userId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid")
                );
        Assertions.assertJsonByName(responseUserData, "email", userData.get("email"));
    }


    @Test
    @Description("This test checks that user is not able to change first name to a new short one (one symbol)")
    @DisplayName("Test update a user with incorrect first name")
    @Severity(NORMAL)
    @Owner("Nurshat Kalimullin")
    @Link(name = "Documentation", url = "https://playground.learnqa.ru/api/map")
    @Epic("User tests")
    @Feature("Non-essential features")
    @Story("Update user")
    public void testEditUserToExtremelyShortFirstName(){
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

        //EDIT
        String newName = "C";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/",
                        userId,
                        editData,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid")

                );
        Assertions.assertJsonByName(responseEditUser, "error", "Too short value for field firstName");

        //GET
        Response responseUserData = apiCoreRequests
                .makeGetRequestById(
                        "https://playground.learnqa.ru/api/user/",
                        userId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid")
                );
        Assertions.assertJsonByName(responseUserData, "firstName", userData.get("firstName"));
    }
}
