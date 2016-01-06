package tr.edu.boun.cmpe.sculture.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.models.response.ErrorResponse;
import tr.edu.boun.cmpe.sculture.models.response.TagResponse;

import static tr.edu.boun.cmpe.sculture.Constants.API_TAG_EDIT;
import static tr.edu.boun.cmpe.sculture.Constants.API_TAG_GET;
import static tr.edu.boun.cmpe.sculture.Constants.BUNDLE_TAG_TITLE;
import static tr.edu.boun.cmpe.sculture.Constants.REQUEST_TAG_DESC_CREATE;
import static tr.edu.boun.cmpe.sculture.Utils.addRequest;

public class TagDescCreateActivity extends AppCompatActivity {
    private EditText contentText;
    private Activity mActivity;
    private String tag_title;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tag_desc);
        mActivity = this;
        actionBar = getSupportActionBar();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            tag_title = bundle.getString(BUNDLE_TAG_TITLE);

        if (tag_title == null)
            return;

        setActionBarTitle(tag_title);

        contentText = (EditText) findViewById(R.id.tagDescription);

        loadTag();
    }

    private void loadTag() {
        JSONObject rb = new JSONObject();
        try {
            rb.put("tag_title", tag_title);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addRequest(API_TAG_GET, rb, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                TagResponse tagResponse = new TagResponse(response);
                contentText.setText(tagResponse.description);
                setActionBarTitle(tagResponse.tag_title);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorResponse errorResponse = new ErrorResponse(error);
                Toast.makeText(getApplicationContext(), R.string.error_occurred, Toast.LENGTH_SHORT).show();
                Log.e("TAG GET", errorResponse.toString());
            }
        }, null);
    }

    private void clickSaveButton() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("tag_description", contentText.getText().toString());
            requestBody.put("tag_title", tag_title);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        addRequest(API_TAG_EDIT, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        TagResponse tagResponse = new TagResponse(response);
                        Intent intent = new Intent(mActivity, TagActivity.class);
                        intent.putExtra(BUNDLE_TAG_TITLE, tagResponse.tag_title);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mActivity.startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ErrorResponse errorResponse = new ErrorResponse(error);
                        Toast.makeText(getApplicationContext(), R.string.error_occurred, Toast.LENGTH_SHORT).show();
                        Log.e("TAG EDIT", errorResponse.toString());
                    }
                }, REQUEST_TAG_DESC_CREATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tag_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save)
            clickSaveButton();
        return true;
    }

    private void setActionBarTitle(String s) {
        if (actionBar != null)
            actionBar.setTitle(s);
    }
}
