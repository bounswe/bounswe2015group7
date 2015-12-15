package tr.edu.boun.cmpe.sculture.models.response;

import org.json.JSONException;
import org.json.JSONObject;

public class UserFollowResponse {

    public boolean is_follow;

    public UserFollowResponse(JSONObject object) {
        try {
            this.is_follow = object.getBoolean("_follow");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
