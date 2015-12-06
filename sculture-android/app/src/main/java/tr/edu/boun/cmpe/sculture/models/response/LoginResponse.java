package tr.edu.boun.cmpe.sculture.models.response;


import org.json.JSONException;
import org.json.JSONObject;

public class LoginResponse {
    public long id;
    public String username;
    public String email;
    public String fullname;
    public long facebook_id;
    public String access_token;
    public boolean is_promoted;
    public int notification_rate;


    public LoginResponse(JSONObject object) {
        try {
            this.id = object.getLong("id");
            this.username = object.getString("username");
            this.email = object.getString("email");
            this.fullname = object.getString("fullname");
            this.facebook_id = object.getLong("facebook_id");
            this.access_token = object.getString("access_token");
            //this.is_promoted = object.getBoolean("is_promoted");
            this.notification_rate = object.getInt("notification_rate");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
