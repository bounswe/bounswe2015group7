package tr.edu.boun.cmpe.sculture.models.response;

import org.json.JSONException;
import org.json.JSONObject;

public class StoryReportResponse {

    public long report_count;

    public StoryReportResponse(JSONObject object) {
        try {
            this.report_count = object.getLong("report_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
