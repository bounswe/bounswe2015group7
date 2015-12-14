package tr.edu.boun.cmpe.sculture.models.response;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FullStoryResponse extends BaseStoryResponse {
    public String content;

    public List<String> media = new ArrayList<>();

    public int vote;

    public FullStoryResponse(JSONObject object) {
        super(object);

        try {
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
