package tr.edu.boun.cmpe.sculture.models.response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommentListResponse {
    public List<CommentResponse> result = new ArrayList<>();

    public CommentListResponse(JSONObject object) {
        try {
            JSONArray array = object.getJSONArray("result");
            for (int i = 0; i < array.length(); i++) {
                result.add(new CommentResponse(array.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}