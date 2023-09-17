import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.*;

public class SecondHomeWorkTests {

    @Test
    public void testGetJson() {
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();
        response.prettyPrint();
        String second =
                response
                        .get("messages[1].message");
        System.out.println(second);
    }


    @Test
    public void testRedirect() {
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        String locationHeader = response.getHeader("Location");
        System.out.println(locationHeader);
    }


    @Test
    public void testRedirects() {
        String url = "https://playground.learnqa.ru/api/long_redirect";
        int statusCode = 301;
        while (statusCode == 301) {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(url)
                    .andReturn();
            statusCode = response.statusCode();
            url = response.getHeader("Location");
            System.out.println(statusCode);
        }
    }

    @Test
    public void testToken() throws InterruptedException {
        String url = "https://playground.learnqa.ru/ajax/api/longtime_job";

        JsonPath response = RestAssured
                .get(url)
                .jsonPath();

        String token =
                response
                        .get("token");
        Map<String, Object> body = new HashMap<>();
        body.put("token", token);

        int seconds =
                response
                        .get("seconds");

        JsonPath responseBeforeFinish = RestAssured
                .given()
                .queryParams(body)
                .get(url)
                .jsonPath();
        String status =
                responseBeforeFinish
                        .get("status");
        System.out.println(status);

        Thread.sleep(seconds * 1000L);

        JsonPath responseAfterFinish = RestAssured
                .given()
                .queryParams(body)
                .get(url)
                .jsonPath();
        String result =
                responseAfterFinish
                        .get("result");
        status =
                responseAfterFinish
                        .get("status");
        System.out.println(result);
        System.out.println(status);
    }


    @Test
    public void testPassword() {
        String result = "";
        String password = "";
        Set<String> passwords = new HashSet<>(Arrays.asList(
                "123456",
                "123456789",
                "qwerty",
                "password",
                "1234567",
                "12345678",
                "12345",
                "iloveyou",
                "111111",
                "123123",
                "abc123",
                "qwerty123",
                "1q2w3e4r",
                "admin",
                "qwertyuiop",
                "654321",
                "555555",
                "lovely",
                "7777777",
                "welcome",
                "888888",
                "princess",
                "dragon",
                "password1",
                "123qwe"));
        Iterator<String> passwordsIterator = passwords.iterator();

        while (!result.equals("You are authorized") && passwordsIterator.hasNext()) {
            Map<String, Object> authData = new HashMap<>();
            password = passwordsIterator.next();
            authData.put("login", "super_admin");
            authData.put("password", password);

            Response responseGetSecret= RestAssured
                    .given()
                    .body(authData)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();
            String authCookie = responseGetSecret.getCookie("auth_cookie");

            Map<String, String> cookies = new HashMap<>();
            cookies.put("auth_cookie", authCookie);
            Response responseCheckCookie= RestAssured
                    .given()
                    .body(authData)
                    .cookies(cookies)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();
            result = responseCheckCookie.asString();
        }
        System.out.println("Password is " + password);
        System.out.println(result);
    }

}
