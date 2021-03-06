package tr.edu.boun.cmpe.sculture;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ocpsoft.pretty.time.PrettyTime;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static tr.edu.boun.cmpe.sculture.BaseApplication.baseApplication;
import static tr.edu.boun.cmpe.sculture.Constants.API_IMAGE_UPLOAD;
import static tr.edu.boun.cmpe.sculture.Constants.HEADER_ACCESS_TOKEN;

public class Utils {

    /**
     * Checks whether the given email has valid syntax
     *
     * @param enteredEmail email address
     * @return True: valid, false: invalid
     */
    public static boolean isEmailValid(String enteredEmail) {
        String EMAIL_REGEX =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(enteredEmail);
        return !enteredEmail.isEmpty() && matcher.matches();
    }

    /**
     * Checks whether the password is valid
     * Password should be longer than 5 chars
     *
     * @param password Password text
     * @return True: valid, false: invalid
     */
    public static boolean isPasswordValid(String password) {
        return !(password == null || password.isEmpty() || password.length() < 6);
    }

    /**
     * Checks whether the username is valid
     *
     * @param username Username
     * @return True: valid, false: invalid
     */
    public static boolean isUserNameValid(String username) {
        return !(username == null || username.isEmpty());
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
        if (!isOnline()) {
            Toast.makeText(baseApplication.getApplicationContext(), R.string.no_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody, listener, errorListener) {
            public HashMap<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<>();
                params.put(HEADER_ACCESS_TOKEN, baseApplication.getTOKEN());
                return params;
            }
        };
        request.setTag(tag);
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        baseApplication.mRequestQueue.add(request);
    }

    /**
     * Adds a request which uploads an image to the server
     *
     * @param uri           The local uri of the image
     * @param listener      The listener function which will be called when the upload is completed successfully.
     * @param errorListener The error function which will be called when the upload fails.
     */
    public static void uploadImage(Uri uri, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        if (!isOnline()) {
            Toast.makeText(baseApplication.getApplicationContext(), R.string.no_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        byte[] byteArray = new byte[0];
        try {
            ParcelFileDescriptor parcelFileDescriptor = baseApplication.getApplicationContext().getContentResolver().openFileDescriptor(uri, "r");
            if (parcelFileDescriptor != null) {
                FileDescriptor fileDescriptor;
                fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                parcelFileDescriptor.close();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        final byte[] finalByteArray = byteArray;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, API_IMAGE_UPLOAD, new JSONObject(), listener, errorListener) {
            public HashMap<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<>();
                params.put(HEADER_ACCESS_TOKEN, baseApplication.getTOKEN());
                return params;
            }

            public byte[] getBody() {
                return finalByteArray;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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

    /**
     * Checks whether there is internet connection or not
     *
     * @return whether there is internet connection or not
     */
    public static boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) baseApplication.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Saves a image whose uri is given to the destination
     * @param source Source uri
     * @param destination Destination path
     * @return New uri
     */
    public static Uri saveImage(Uri source, String destination) {

        try {
            ParcelFileDescriptor parcelFileDescriptor = baseApplication.getApplicationContext().getContentResolver().openFileDescriptor(source, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            FileOutputStream out = new FileOutputStream(destination);
            image.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
            return Uri.fromFile(new File(destination));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}