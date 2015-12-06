package tr.edu.boun.cmpe.sculture.models.response;


import org.json.JSONException;
import org.json.JSONObject;

public class FullStoryResponse extends BaseStoryResponse {
    public String content;

    public FullStoryResponse(JSONObject object) {
        super(object);

        try {
            this.content = object.getString("content");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
