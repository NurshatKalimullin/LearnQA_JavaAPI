import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThirdHomeWorkTests {

    @Test
    public void testStringLength() {
        String text = "test test test test test";
        assertTrue(text.length() > 15, "The text contains less than 15 symbols");
    }


    @Test
    public void testCookie() {
        Response response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        assertEquals(response.getCookie("HomeWork"), "hw_value", "HomeWork cookie value is incorrect");
    }


    @Test
    public void testHeader() {
        Response response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();
        assertEquals(response.getHeader("x-secret-homework-header"),
                "Some secret value",
                "x-secret-homework-header header value is incorrect");
    }


    @ParameterizedTest
    @CsvSource(delimiter = '?',
            value = {
            "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30?Mobile?No?Android",
            "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1?Mobile?Chrome?iOS",
            "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)?Googlebot?Unknown?Unknown",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0?Web?Chrome?No",
            "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1?Mobile?No?iPhone"
    })
    public void testUserAgent(String userAgent, String platform, String browser, String device) {
        Map<String, String> headers = new HashMap<>();
        if (userAgent.length() > 0) {
            headers.put("User-Agent", userAgent);
        }
        JsonPath response = RestAssured
                .given()
                .headers(headers)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .jsonPath();
        String resultPlatform = response.getString("platform");
        String resultBrowser = response.getString("browser");
        String resultDevice = response.getString("device");

        System.out.println(resultPlatform + " " + resultBrowser + " " + resultDevice);
        assertEquals(
                platform,
                resultPlatform,
                String.format("For User-Agent %s expected platform %s is not equal to result platform %s",
                        userAgent,
                        platform,
                        resultPlatform));
        assertEquals(
                browser,
                resultBrowser,
                String.format("For User-Agent %s expected platform %s is not equal to result platform %s",
                        userAgent,
                        browser,
                        resultBrowser)
        );
        assertEquals(
                device,
                resultDevice,
                String.format("For User-Agent %s expected platform %s is not equal to result platform %s",
                        userAgent,
                        device,
                        resultDevice)
        );
    }
}
