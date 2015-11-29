package tr.edu.boun.cmpe.sculture.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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
import tr.edu.boun.cmpe.sculture.adapter.StoryImageViewAdapter;
import tr.edu.boun.cmpe.sculture.view.TagView;

import static tr.edu.boun.cmpe.sculture.Constants.API_STORY_CREATE;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_CONTENT;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_TAGS;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_TITLE;
import static tr.edu.boun.cmpe.sculture.Constants.REQUEST_TAG_STORY_CREATE;
import static tr.edu.boun.cmpe.sculture.Utils.addRequest;

public class StoryCreateActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 42;

    EditText titleText;
    EditText contentText;
    Activity mActivity;
    TagView completionView;

    private RecyclerView mRecyclerView;
    private StoryImageViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_story_create);
        titleText = (EditText) findViewById(R.id.title);
        contentText = (EditText) findViewById(R.id.content);

        completionView = (TagView) findViewById(R.id.searchView);
        //Even though we don't use autocompletion the library require this adapter.
        completionView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[0]));

        completionView.performValidation();


        mRecyclerView = (RecyclerView) findViewById(R.id.image_recyclerView);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        ArrayList<Uri> myDataset = new ArrayList<>();

        mAdapter = new StoryImageViewAdapter(myDataset, this, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

    }


    private void clickCreateButton() {
        String title = titleText.getText().toString();
        String content = contentText.getText().toString();


        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(FIELD_TITLE, title);
            requestBody.put(FIELD_CONTENT, content);
            JSONArray tags = new JSONArray();
            List<String> ts = completionView.getObjects();
            for (String t : ts)
                tags.put(t);
            requestBody.put(FIELD_TAGS, tags);
            //TODO Image upload
        } catch (JSONException e) {
            e.printStackTrace();
        }


        addRequest(API_STORY_CREATE, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        startActivity(new Intent(mActivity, MainActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mActivity, new String(error.networkResponse.data), Toast.LENGTH_LONG).show();
                        //TODO ERROR HANDLING
                    }
                }, REQUEST_TAG_STORY_CREATE);


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
            clickCreateButton();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i("HERE", "Uri: " + uri.toString());
                mAdapter.addElement(uri);
            }
        }
    }
}
