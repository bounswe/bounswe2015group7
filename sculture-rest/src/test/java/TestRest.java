import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import sculture.Application;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static sculture.Utils.checkEmailSyntax;

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
        jsonResponse = Unirest.post("http://127.0.0.1:8080/user/register")
                .header("Content-Type", "application/json")
                .body(jsonNode)
                .asJson();
        assertEquals(jsonResponse.getBody().getObject().getString("username"), "test-user");
        assertEquals(jsonResponse.getBody().getObject().getString("email"), "test-password@test.com");
        
        String access_token = jsonResponse.getBody().getObject().getString("access_token");
        Map map = new HashMap<String,String>();
        map.put("Content-Type", "application/json");
        map.put("access-token",access_token);
        jsonObject = new JSONObject();
        jsonObject.put("title","title");
        jsonObject.put("content","content");
        jsonNode = new JsonNode(jsonObject.toString());
        jsonResponse = Unirest.post("http://127.0.0.1:8080/story/create")
                .headers(map)
                .body(jsonNode)
                .asJson();
        assertEquals(200, jsonResponse.getStatus());


        map = new HashMap<String,String>();
        map.put("Content-Type", "application/json");
        map.put("access-token",access_token);
        jsonObject = new JSONObject();
        jsonObject.put("story_id","1");
        jsonObject.put("isPositive","true");
        jsonObject.put("user_id","1");
        jsonNode = new JsonNode(jsonObject.toString());
        jsonResponse = Unirest.post("http://127.0.0.1:8080/story/vote")
                .headers(map)
                .body(jsonNode)
                .asJson();
        assertEquals(200, jsonResponse.getStatus());

        map = new HashMap<String,String>();
        map.put("Content-Type", "application/json");
        map.put("access-token",access_token);
        jsonObject = new JSONObject();
        jsonObject.put("story_id","1");
        jsonObject.put("isPositive","true");
        jsonObject.put("user_id","1");
        jsonNode = new JsonNode(jsonObject.toString());
        jsonResponse = Unirest.post("http://127.0.0.1:8080/story/vote")
                .headers(map)
                .body(jsonNode)
                .asJson();
        assertEquals(200, jsonResponse.getStatus());


    }
    @Test
    public void validateEmail(){
        assertFalse(checkEmailSyntax(null));
        assertFalse(checkEmailSyntax(""));
        assertFalse(checkEmailSyntax("bilal"));
        assertTrue(checkEmailSyntax("bilal@bilal.com"));
    }
}
