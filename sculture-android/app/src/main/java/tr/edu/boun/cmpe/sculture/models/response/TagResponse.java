package tr.edu.boun.cmpe.sculture.models.response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class TagResponse {

    public String tag_title;
    public String description;
    public boolean is_location;
    public long last_editor_id;
    public Date last_edit_date;
    private String last_editor_name;

    public TagResponse(JSONObject object) {
        try {
            this.tag_title = object.getString("tag_title");
            this.description = object.getString("description");
            this.is_location = object.getBoolean("is_location");
            this.last_editor_id = object.getLong("last_editor_id");
            this.last_edit_date = new Date(object.getLong("last_edit_date"));
            this.last_editor_name = object.getString("last_editor_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
