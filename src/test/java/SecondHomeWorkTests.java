import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
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
}
