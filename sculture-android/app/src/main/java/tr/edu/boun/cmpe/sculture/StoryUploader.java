package tr.edu.boun.cmpe.sculture;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tr.edu.boun.cmpe.sculture.fragment.main.ProfileFragment;
import tr.edu.boun.cmpe.sculture.models.response.ImageResponse;

import static tr.edu.boun.cmpe.sculture.Constants.API_STORY_CREATE;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_CONTENT;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_TAGS;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_TITLE;
import static tr.edu.boun.cmpe.sculture.Constants.REQUEST_TAG_STORY_CREATE;
import static tr.edu.boun.cmpe.sculture.Utils.addRequest;


public class StoryUploader {
    ArrayList<Uri> uris;
    String title;
    String content;
    List<String> tags;
    ArrayList<String> image_ids = new ArrayList<>();

    public StoryUploader(String title, String content, List<String> tags, ArrayList<Uri> uris) {
        this.content = content;
        this.title = title;
        this.tags = tags;
        this.uris = uris;
        upload();
    }


    private void upload() {
        if (uris.size() == 0) {
            JSONObject requestBody = new JSONObject();
            try {
                requestBody.put(FIELD_TITLE, title);
                requestBody.put(FIELD_CONTENT, content);
                JSONArray tagsJ = new JSONArray();

                for (String t : tags)
                    tagsJ.put(t);
                requestBody.put(FIELD_TAGS, tagsJ);

                if (image_ids.size() > 0) {
                    JSONArray medias = new JSONArray();
                    for (String id : image_ids)
                        medias.put(id);
                    requestBody.put("media", medias);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            addRequest(API_STORY_CREATE, requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("HERE", response.toString());
                            ProfileFragment.reset();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                VolleyError e = new VolleyError(new String(error.networkResponse.data));
                                Log.i("ERROR", e.toString());
                            } else Log.i("ERROR", error.toString());
                            //TODO ERROR HANDLING
                        }
                    }, REQUEST_TAG_STORY_CREATE);


        } else {
            Log.i("HERE", "uaa");
            Uri u = uris.get(0);
            uris.remove(0);
            Utils.uploadImage(u, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    ImageResponse imageResponse = new ImageResponse(response);
                    image_ids.add(imageResponse.id);
                    upload();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        VolleyError e = new VolleyError(new String(error.networkResponse.data));
                        Log.i("ERROR", e.toString());
                    } else Log.i("ERROR", error.toString());
                    //TODO ERROR HANDLING
                }
            });
        }
    }


}
