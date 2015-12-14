package tr.edu.boun.cmpe.sculture.models.response;

import org.json.JSONException;
import org.json.JSONObject;

public class VoteResponse {

    private long positive_vote;
    private long negative_vote;
    private int vote;

    public VoteResponse(JSONObject object) {
        try {
            this.positive_vote = object.getLong("positive_vote");
            this.negative_vote = object.getLong("negative_vote");
            this.vote = object.getInt("vote");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
