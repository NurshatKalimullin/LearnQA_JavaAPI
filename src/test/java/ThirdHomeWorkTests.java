import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

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
}
