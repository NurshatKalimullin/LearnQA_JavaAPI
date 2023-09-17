import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

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
}
