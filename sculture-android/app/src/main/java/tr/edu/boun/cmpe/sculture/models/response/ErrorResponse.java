package tr.edu.boun.cmpe.sculture.models.response;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class ErrorResponse {
    public long timestamp;
    public int status;
    public String error;
    public String exception;
    public String message = "";
    public String path;

    private String full;

    public ErrorResponse(VolleyError volleyError) {
        if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
            try {
                full = new String(volleyError.networkResponse.data);
                JSONObject object = new JSONObject(full);
                timestamp = object.getLong("timestamp");
                status = object.getInt("status");
                error = object.getString("error");
                exception = object.getString("exception");
                message = object.getString("message");
                path = object.getString("path");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            full = volleyError.toString();
        }
    }

    public String toString() {
        return full;
    }
}
