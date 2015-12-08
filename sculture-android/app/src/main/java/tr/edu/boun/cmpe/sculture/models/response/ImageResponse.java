package tr.edu.boun.cmpe.sculture.models.response;

import org.json.JSONException;
import org.json.JSONObject;

public class ImageResponse {

    public String id;

    public ImageResponse(JSONObject object) {
        try {
            this.id = object.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
