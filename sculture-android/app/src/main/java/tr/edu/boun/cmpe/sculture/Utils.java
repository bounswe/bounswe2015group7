package tr.edu.boun.cmpe.sculture;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ocpsoft.pretty.time.PrettyTime;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

import static tr.edu.boun.cmpe.sculture.BaseApplication.baseApplication;
import static tr.edu.boun.cmpe.sculture.Constants.HEADER_ACCESS_TOKEN;

public class Utils {

    public static boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    public static boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public static boolean isUserNameValid(String username) {
        //TODO Replace
        return username.length() > 3;
    }


    public static String timespamptToPrettyStrig(long timesmap) {
        Date date = new Date();
        date.setTime(timesmap);

        PrettyTime p = new PrettyTime();

        return p.format(date);

    }

    public static JsonObjectRequest addRequest(String url, JSONObject requestBody, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, Object tag) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody.toString(), listener, errorListener) {
            public HashMap<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<>();
                params.put(HEADER_ACCESS_TOKEN, baseApplication.getTOKEN());
                return params;
            }
        };
        request.setTag(tag);

        baseApplication.mRequestQueue.add(request);

        return request;
    }

    public static void removeRequests(Object tag) {
        baseApplication.mRequestQueue.cancelAll(tag);
    }

}
