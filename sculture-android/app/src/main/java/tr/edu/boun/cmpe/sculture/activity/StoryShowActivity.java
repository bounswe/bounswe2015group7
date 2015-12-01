package tr.edu.boun.cmpe.sculture.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.adapter.RecyclerViewAdaptor_Comments;

import static tr.edu.boun.cmpe.sculture.Constants.API_COMMENT_GET;
import static tr.edu.boun.cmpe.sculture.Constants.API_STORY_GET;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_CONTENT;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_CREATION_DATE;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_ID;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_LAST_EDITOR;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_OWNER;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_TAGS;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_TITLE;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_UPDATE_DATE;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_USERNAME;
import static tr.edu.boun.cmpe.sculture.Constants.REQUEST_TAG_COMMENT_GET;
import static tr.edu.boun.cmpe.sculture.Constants.REQUEST_TAG_STORY_GET;
import static tr.edu.boun.cmpe.sculture.Utils.addRequest;
import static tr.edu.boun.cmpe.sculture.Utils.timestampToPrettyString;

public class StoryShowActivity extends AppCompatActivity {

    private static final int SIZE = 10;
    private int PAGE = 1;
    TextView title;
    TextView content;
    TextView tags;
    TextView storyOwner;
    TextView createdAt;
    TextView lastEditor;
    TextView lastUpdatedAt;
    RecyclerView rcw;
    LinearLayoutManager mLayoutManager;
    RecyclerViewAdaptor_Comments adaptor;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_show);
        bundle = getIntent().getExtras();
        title = (TextView) findViewById(R.id.story_title);
        content = (TextView) findViewById(R.id.storyContent);
        tags = (TextView) findViewById(R.id.storyTags);
        storyOwner = (TextView) findViewById(R.id.storyOwner);
        createdAt = (TextView) findViewById(R.id.storyCreatedAt);
        lastEditor = (TextView) findViewById(R.id.storyLastEditor);
        lastUpdatedAt = (TextView) findViewById(R.id.story_update_date);
        if (bundle.getLong("id") != 0) {
            getStory(bundle.getLong("id"));
        }

        rcw = (RecyclerView) findViewById(R.id.comment_recycler);
        rcw.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        adaptor = new RecyclerViewAdaptor_Comments(this);
        rcw.setLayoutManager(mLayoutManager);
        rcw.setAdapter(adaptor);
        rcw.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                load_comment();
            }
        });

    }

    private void load_comment() {
        final JSONObject requestBody = new JSONObject();
        try {
            if (bundle.getLong("id") != 0) {
                requestBody.put("story_id", bundle.getLong("id"));
               // requestBody.put(FIELD_SIZE, SIZE);
               // requestBody.put(FIELD_PAGE, PAGE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int totalItemCount = mLayoutManager.getItemCount();
        int lastVisibleIndex = mLayoutManager.findLastVisibleItemPosition();
        boolean loadMore = lastVisibleIndex == totalItemCount - 1;

        addRequest(API_COMMENT_GET, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray array;
                try {
                    array = response.getJSONArray("comment");
                    for (int i = 0; i < array.length(); i++)
                        adaptor.addElement(array.getJSONObject(i));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, REQUEST_TAG_COMMENT_GET);
    }


    public void getStory(long id) {

        // TODO get story request

        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put(FIELD_ID, id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        addRequest(API_STORY_GET, requestObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            title.setText(response.getString(FIELD_TITLE));
                            content.setText(response.getString(FIELD_CONTENT));
                            JSONArray tagsAr = response.getJSONArray(FIELD_TAGS);
                            String tagstring = "";
                            for (int i = 0; i < tagsAr.length(); i++)
                                tagstring += tagsAr.get(i) + ", ";
                            tags.setText(tagstring);
                            storyOwner.setText(response.getJSONObject(FIELD_OWNER).getString(FIELD_USERNAME));
                            lastEditor.setText(response.getJSONObject(FIELD_LAST_EDITOR).getString(FIELD_USERNAME));
                            createdAt.setText(timestampToPrettyString(response.getLong(FIELD_CREATION_DATE)));
                            lastUpdatedAt.setText(timestampToPrettyString(response.getLong(FIELD_UPDATE_DATE)));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                },
                REQUEST_TAG_STORY_GET);


//        ParseCloud.callFunctionInBackground("story_get", param, new FunctionCallback<HashMap>() {
//
//                    @Override
//                    public void done(HashMap item, ParseException e) {
//                        if (e == null) {
//
//
//
//                            ArrayList<String> story = new ArrayList<String>();
//
//                            ArrayList<String> tags2 = (ArrayList<String>) item.get("tags");
//                            String all = "";
//                            for (String tagsAll : tags2) {
//                                all += tagsAll + " ";
//                            }
//
//                            tags.setText(all);
//                            content.setText("" + (String) item.get("content"));
//
//                            storyOwner.setText("Story Owner: " + (String) item.get("ownerId"));//ps.getObjectId());
//                            String cr = "" + item.get("createdAt");
//                            createdAt.setText("Story creation Time: " + cr.substring(0, 10) + " " + cr.substring(11, 19));
//                            cr = "" + item.get("updatedAt");
//                            lastEditor.setText("Last Editor:" + (String) item.get("lastEditorId").toString());
//                            lastUpdatedAt.setText("Last Update Time: " + cr.substring(0, 10) + " " + cr.substring(11, 19));
//
//
//
//                        } else
//                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        );
    }


}
