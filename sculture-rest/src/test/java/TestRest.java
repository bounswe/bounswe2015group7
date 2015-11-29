import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import sculture.Application;

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
    public void test() throws Exception {

        HttpResponse<JsonNode> jsonResponse = null;
        try {


            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", "test-user");
            jsonObject.put("password", "test-password");
            jsonObject.put("email", "test-password@test.com");


            JsonNode jsonNode = new JsonNode(jsonObject.toString());
            jsonResponse = Unirest.post("http://52.28.216.93:9000/user/register")
                    .header("Content-Type", "application/json")
                    .body(jsonNode)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        if (jsonResponse != null) {
            System.out.println(jsonResponse.getBody());
        }
    }
}
