package tr.edu.boun.cmpe.sculture.models.response;

import org.json.JSONException;
import org.json.JSONObject;

public class UserGetResponse {

    public long user_id;
    public String username;
    public String email;
    public boolean is_following;

    public UserGetResponse(JSONObject object) {
        try {
            this.user_id = object.getLong("user_id");
            this.username = object.getString("username");
            this.email = object.getString("email");
            this.is_following = object.getBoolean("_following");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
