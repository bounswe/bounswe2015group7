package tr.edu.boun.cmpe.sculture;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ocpsoft.pretty.time.PrettyTime;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
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

    /**
     * Converts date to string such as (seconds ago, 1 month ago etc.
     *
     * @param date Date
     * @return Pretty date string
     */
    public static String timestampToPrettyString(Date date) {
        PrettyTime p = new PrettyTime();
        return p.format(date);
    }

    /**
     * Create a request with access_token and add it to queue. When the request done the listener will be triggered.
     *
     * @param url           The url of the request. Use API url constants.
     * @param requestBody   The JSON request body
     * @param listener      The listener which will be triggered with successful response
     * @param errorListener The listener which will be triggered with error response
     * @param tag           The tag of the request. It may be useful to remove request from the queue. It may be null.
     */
    public static void addRequest(String url, JSONObject requestBody, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, Object tag) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody.toString(), listener, errorListener) {
            public HashMap<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<>();
                params.put(HEADER_ACCESS_TOKEN, baseApplication.getTOKEN());
                return params;
            }
        };
        request.setTag(tag);
        baseApplication.mRequestQueue.add(request);
    }

    public static void uploadImage(Uri uri, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        byte[] byteArray = new byte[0];
        try {
            ParcelFileDescriptor parcelFileDescriptor = baseApplication.getApplicationContext().getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final byte[] finalByteArray = byteArray;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BuildConfig.API_BASE_URL + "/image/upload", listener, errorListener) {
            public HashMap<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<>();
                params.put(HEADER_ACCESS_TOKEN, baseApplication.getTOKEN());
                return params;
            }
            public byte[] getBody() {
                return finalByteArray;
            }
        };
        baseApplication.mRequestQueue.add(request);
    }

    /**
     * Removes requests with given tag from the queue. The listeners of the removed requests will not be triggered.
     *
     * @param tag The tag of the request
     */
    public static void removeRequests(Object tag) {
        baseApplication.mRequestQueue.cancelAll(tag);
    }

}
