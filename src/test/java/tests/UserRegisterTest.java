package tests;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static io.qameta.allure.SeverityLevel.BLOCKER;
import static io.qameta.allure.SeverityLevel.CRITICAL;

@Epic("User tests")
@Feature("Essential features")
@Story("Register user")
public class UserRegisterTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("This test checks that registration of duplicate emails are not allowed")
    @DisplayName("Test register with existing email")
    @Severity(CRITICAL)
    @Owner("Nurshat Kalimullin")
    @Link(name = "Documentation", url = "https://playground.learnqa.ru/api/map")
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
    }


    @Test
    @Description("This test successfully creates new user")
    @DisplayName("Test register new user")
    @Severity(BLOCKER)
    @Owner("Nurshat Kalimullin")
    @Link(name = "Documentation", url = "https://playground.learnqa.ru/api/map")
    public void testCreateUserSuccessfully() {
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasKey(responseCreateAuth, "id");
    }


    @Test
    @Description("This test checks that registration of duplicate emails are not allowed")
    @DisplayName("Test register with existing email")
    @Severity(CRITICAL)
    @Owner("Nurshat Kalimullin")
    @Link(name = "Documentation", url = "https://playground.learnqa.ru/api/map")
    public void testCreateUserWithIncorrectEmail() {
        String email = "vinkotovexample.com";
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateUser = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateUser, 400);
        Assertions.assertResponseTextEquals(responseCreateUser, "Invalid email format");
    }


    @ParameterizedTest(name = "{displayName} {argumentsWithNames}")
    @ValueSource(strings = {"email", "password", "username", "firstName", "lastName",})
    @Description("This test checks that registration without required fields are not allowed")
    @DisplayName("Test register without required field")
    @Severity(CRITICAL)
    @Owner("Nurshat Kalimullin")
    @Link(name = "Documentation", url = "https://playground.learnqa.ru/api/map")
    public void testCreateUserWithoutRequiredKey(String key) {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        userData.remove(key);

        Response responseCreateUser = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateUser, 400);
        Assertions.assertResponseTextEquals(
                responseCreateUser,
                "The following required params are missed: " + key
        );
    }


    @Test
    @Description("This test checks that registration with single letter first name is prohibited")
    @DisplayName("Test register with single letter first name")
    @Severity(CRITICAL)
    @Owner("Nurshat Kalimullin")
    @Link(name = "Documentation", url = "https://playground.learnqa.ru/api/map")
    public void testCreateUserWithExtremelyShortFirstName() {
        String firstName = "A";
        Map<String, String> userData = new HashMap<>();
        userData.put("firstName", firstName);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateUser = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateUser, 400);
        Assertions.assertResponseTextEquals(responseCreateUser, "The value of 'firstName' field is too short");
    }


    @Test
    @Description("This test checks that registration with very long first name is not possible")
    @DisplayName("Test register with extremely long first name")
    @Severity(CRITICAL)
    @Owner("Nurshat Kalimullin")
    @Link(name = "Documentation", url = "https://playground.learnqa.ru/api/map")
    public void testCreateUserWithExtremelyLongFirstName() {
        String firstName = "cfcJgfJYxnBpxUAmJGwzighuagUyNQtCyMTdyTieqCHnnaDrXuzMDcLLgMXzggHdmdriHdCUYBWTGKdwAV" +
                "CbneaxyBBgfMPnWgrJHHtpwMZaxaeudkEHrxPDvBaMkVwQmdJPRCydbWyDiSudyuDWdpWCxVLWGcQWLQgHVhCnxMzLebzBFV" +
                "NvhamFFennNDdSzRJmzfeMbPJRjKaijwUapcqBCPBcbBgKSBtRRzvrSndgvrfZcYambecitxdgderRE";
        Map<String, String> userData = new HashMap<>();
        userData.put("firstName", firstName);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateUser = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateUser, 400);
        Assertions.assertResponseTextEquals(responseCreateUser, "The value of 'firstName' field is too long");
    }
}
