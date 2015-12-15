package tr.edu.boun.cmpe.sculture.models.response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StoryResponse {
    public class User {
        public long id;
        public String username;
    }

    public long id;
    public String title;
    public Date creation_date;
    public Date update_date;
    public User last_editor = new User();
    public User owner = new User();
    public List<String> tags;
    public long positive_vote;
    public long negative_vote;
    public long report_count;
    public String content;
    public List<String> media = new ArrayList<>();
    public int vote;

    public StoryResponse(JSONObject object) {
        try {
            this.id = object.getLong("id");
            this.title = object.getString("title");
            this.creation_date = new Date(object.getLong("creation_date"));
            this.update_date = new Date(object.getLong("update_date"));
            this.last_editor.id = object.getJSONObject("last_editor").getLong("id");
            this.owner.id = object.getJSONObject("owner").getLong("id");
            this.positive_vote = object.getLong("positive_vote");
            this.negative_vote = object.getLong("negative_vote");
            this.report_count = object.getLong("report_count");
            this.tags = new ArrayList<>();

            JSONArray tagsArray = object.getJSONArray("tags");
            for (int i = 0; i < tagsArray.length(); i++) {
                tags.add(tagsArray.getString(i));
            }

            this.owner.username = object.getJSONObject("owner").getString("username");
            this.last_editor.username = object.getJSONObject("last_editor").getString("username");
            this.content = object.getString("content");
            this.vote = object.getInt("vote");

            JSONArray mediaArray = object.getJSONArray("media");
            for (int i = 0; i < mediaArray.length(); i++) {
                media.add(mediaArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
