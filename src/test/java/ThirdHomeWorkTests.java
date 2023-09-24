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
}
