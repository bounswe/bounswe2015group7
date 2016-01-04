package tr.edu.boun.cmpe.sculture;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tr.edu.boun.cmpe.sculture.fragment.main.ProfileFragment;
import tr.edu.boun.cmpe.sculture.models.response.ErrorResponse;
import tr.edu.boun.cmpe.sculture.models.response.ImageResponse;

import static tr.edu.boun.cmpe.sculture.Constants.API_STORY_CREATE;
import static tr.edu.boun.cmpe.sculture.Constants.API_STORY_EDIT;
import static tr.edu.boun.cmpe.sculture.Constants.ERROR_INVALID_ACCESS_TOKEN;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_CONTENT;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_TAGS;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_TITLE;
import static tr.edu.boun.cmpe.sculture.Constants.REQUEST_TAG_STORY_CREATE;
import static tr.edu.boun.cmpe.sculture.Utils.addRequest;

/**
 * A class which handles background story uploading with image uploads
 */
public class StoryUploader {
    ArrayList<Uri> uris = new ArrayList<>();
    String title;
    String content;
    List<String> tags;
    ArrayList<String> image_ids = new ArrayList<>();
    boolean isEdit = false;
    long id;

    /**
     * Creates a story and uploads it to the server
     *
     * @param title          Title of the story
     * @param content        Content of the story
     * @param tags           Tags of the story
     * @param imageLocations Image locations of the story
     */
    public StoryUploader(String title, String content, List<String> tags, ArrayList<ImageLocation> imageLocations) {
        this.content = content;
        this.title = title;
        this.tags = tags;
        for (ImageLocation imageLocation : imageLocations) {
            if (imageLocation.isLocal())
                uris.add(imageLocation.getUri());
            else
                image_ids.add(imageLocation.getId());
        }
        new UploadStoryTask().execute(this);
    }

    /**
     * Edits an existing story on database
     *
     * @param id             ID of the story
     * @param title          Title of the story
     * @param content        Content of the story
     * @param tags           Tags of the story
     * @param imageLocations Image locations of the story
     */
    public StoryUploader(long id, String title, String content, List<String> tags, ArrayList<ImageLocation> imageLocations) {
        this.id = id;
        this.isEdit = true;
        this.content = content;
        this.title = title;
        this.tags = tags;
        for (ImageLocation imageLocation : imageLocations) {
            if (imageLocation.isLocal())
                uris.add(imageLocation.getUri());
            else
                image_ids.add(imageLocation.getId());
        }
        new UploadStoryTask().execute(this);
    }

    /**
     * Uploads the story and images
     */
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

                if (isEdit)
                    requestBody.put("story_id", id);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String API_URL = API_STORY_CREATE;
            if (isEdit)
                API_URL = API_STORY_EDIT;
            addRequest(API_URL, requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            ProfileFragment.reset();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ErrorResponse errorResponse = new ErrorResponse(error);
                            switch (errorResponse.error) {
                                case ERROR_INVALID_ACCESS_TOKEN:
                                    Toast.makeText(BaseApplication.baseApplication, R.string.access_toke_error, Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(BaseApplication.baseApplication, R.string.error_occurred, Toast.LENGTH_SHORT).show();
                                    Log.e("CREATE", errorResponse.toString());
                                    break;

                            }
                        }
                    }, REQUEST_TAG_STORY_CREATE);


        } else {
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
                    ErrorResponse errorResponse = new ErrorResponse(error);

                    Toast.makeText(BaseApplication.baseApplication, R.string.error_occurred, Toast.LENGTH_SHORT).show();
                    Log.e("UPLOAD", errorResponse.toString());
                }
            });
        }
    }

    /**
     * Async upload starter
     */
    private class UploadStoryTask extends AsyncTask<StoryUploader, Integer, Long> {
        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected Long doInBackground(StoryUploader... params) {
            params[0].upload();
            return null;
        }

        protected void onPostExecute(Long result) {

        }
    }
}
