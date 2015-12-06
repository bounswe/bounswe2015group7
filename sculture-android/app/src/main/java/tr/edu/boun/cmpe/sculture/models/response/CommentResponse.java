package tr.edu.boun.cmpe.sculture.models.response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


public class CommentResponse {
    public long comment_id;
    public long story_id;
    public long owner_id;
    public String owner_username;
    public Date create_date;
    public String content;
    public Date last_edit_date;

    public CommentResponse(JSONObject object) {
        try {
            this.comment_id = object.getLong("comment_id");
            this.story_id = object.getLong("story_id");
            this.owner_id = object.getLong("owner_id");
            this.create_date = new Date(object.getLong("create_date"));
            this.content = object.getString("content");
            this.last_edit_date = new Date(object.getLong("last_edit_date"));
            this.owner_username = object.getString("owner_username");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
