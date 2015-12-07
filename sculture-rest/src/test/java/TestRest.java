import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import sculture.Application;

import static org.junit.Assert.assertEquals;

/**
 * Created by bilal on 29/11/15.
 */
public class TestRest {

    static Application application;

    @BeforeClass
    public static void before() {
        application = new Application();
        application.main(new String[0]);
    }

    @Test
    public void testSample() throws Exception {

        HttpResponse<JsonNode> jsonResponse = null;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "test-user");
        jsonObject.put("password", "test-password");
        jsonObject.put("email", "test-password@test.com");


        JsonNode jsonNode = new JsonNode(jsonObject.toString());
        jsonResponse = Unirest.post("http://52.28.216.93:9000/user/register")
                .header("Content-Type", "application/json")
                .body(jsonNode)
                .asJson();
        assertEquals(jsonResponse.getBody().getObject().getString("username"), "test-user");
        assertEquals(jsonResponse.getBody().getObject().getString("password"), "test-password");
        assertEquals(jsonResponse.getBody().getObject().getString("email"), "test-password@test.com");
    }
}
