package tr.edu.boun.cmpe.sculture.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.StoryUploader;
import tr.edu.boun.cmpe.sculture.adapter.StoryImageViewAdapter;
import tr.edu.boun.cmpe.sculture.models.response.ErrorResponse;
import tr.edu.boun.cmpe.sculture.models.response.StoryResponse;
import tr.edu.boun.cmpe.sculture.view.TagView;

import static tr.edu.boun.cmpe.sculture.Constants.API_STORY_GET;
import static tr.edu.boun.cmpe.sculture.Constants.BUNDLE_IS_EDIT;
import static tr.edu.boun.cmpe.sculture.Constants.BUNDLE_STORY_ID;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_CONTENT;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_ID;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_TAGS;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_TITLE;
import static tr.edu.boun.cmpe.sculture.Utils.addRequest;

public class StoryCreateActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 42;
    private final ArrayList<Uri> mediaUris = new ArrayList<>();
    private EditText titleText;
    private EditText contentText;
    private Activity mActivity;
    private TagView completionView;
    private StoryImageViewAdapter mAdapter;
    private boolean isEdit;
    private long storyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_create);
        mActivity = this;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isEdit = bundle.getBoolean(BUNDLE_IS_EDIT, false);

            if (isEdit) {
                storyId = bundle.getLong(BUNDLE_STORY_ID);
            }
        }

        titleText = (EditText) findViewById(R.id.title);
        contentText = (EditText) findViewById(R.id.content);

        completionView = (TagView) findViewById(R.id.searchView);
        //Even though we don't use autocompletion the library require this adapter.
        completionView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[0]));

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.image_recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(llm);
        mAdapter = new StoryImageViewAdapter(mediaUris, null, true);
        mRecyclerView.setAdapter(mAdapter);

        //If it is edit, pre-fill the view
        if (isEdit) {
            final JSONObject requestBody = new JSONObject();
            try {
                requestBody.put(FIELD_ID, storyId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            addRequest(API_STORY_GET, requestBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    StoryResponse storyResponse = new StoryResponse(response);

                    titleText.setText(storyResponse.title);
                    contentText.setText(storyResponse.content);

                    for (String string : storyResponse.tags)
                        completionView.addObject(string);

                    //TODO Get media files and put on recycler view
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ErrorResponse errorResponse = new ErrorResponse(error);
                    Toast.makeText(getApplicationContext(), R.string.error_occurred, Toast.LENGTH_SHORT).show();
                    Log.e("STORY", errorResponse.toString());

                }
            }, null);
        }
    }


    private void clickSaveButton() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(FIELD_TITLE, titleText.getText());
            requestBody.put(FIELD_CONTENT, contentText.getText());
            JSONArray tags = new JSONArray();
            List<String> ts = completionView.getObjects();
            for (String t : ts)
                tags.put(t);
            requestBody.put(FIELD_TAGS, tags);

            if (isEdit)
                requestBody.put(FIELD_ID, storyId);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        new StoryUploader(titleText.getText().toString(), contentText.getText().toString(), completionView.getObjects(), mediaUris);
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.story_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_media) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, READ_REQUEST_CODE);
            return true;
        }

        if (id == R.id.action_save)
            clickSaveButton();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri;
            if (resultData != null) {
                uri = resultData.getData();
                mAdapter.addElement(uri);
            }
        }
    }
}
