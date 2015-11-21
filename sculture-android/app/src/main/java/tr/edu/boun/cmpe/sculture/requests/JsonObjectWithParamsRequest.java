package tr.edu.boun.cmpe.sculture.requests;

import android.net.Uri;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JsonObjectWithParamsRequest extends JsonObjectRequest {
    public JsonObjectWithParamsRequest(int method, String url, HashMap<String, String> params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, appendUrlParameter(url, params), listener, errorListener);
    }

    public static String appendUrlParameter(String url, HashMap<String, String> params) {
        Uri.Builder builder = Uri.parse(url).buildUpon();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            builder.appendQueryParameter(key, value);
        }
        return builder.build().toString();
    }
}
