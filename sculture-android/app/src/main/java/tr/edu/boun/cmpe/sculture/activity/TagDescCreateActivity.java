package tr.edu.boun.cmpe.sculture.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.models.response.TagResponse;

import static tr.edu.boun.cmpe.sculture.Constants.API_TAG_CREATE;
import static tr.edu.boun.cmpe.sculture.Constants.API_TAG_EDIT;
import static tr.edu.boun.cmpe.sculture.Constants.API_TAG_GET;
import static tr.edu.boun.cmpe.sculture.Constants.BUNDLE_IS_EDIT;
import static tr.edu.boun.cmpe.sculture.Constants.BUNDLE_TAG_TITLE;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_CONTENT;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_ID;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_TITLE;
import static tr.edu.boun.cmpe.sculture.Constants.REQUEST_TAG_DESC_CREATE;
import static tr.edu.boun.cmpe.sculture.Utils.addRequest;

public class TagDescCreateActivity extends AppCompatActivity {
    private TextView titleText;
    private EditText contentText;
    private Activity mActivity;
    private boolean isEdit;
    private String tag_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tag_desc);
        mActivity = this;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isEdit = bundle.getBoolean(BUNDLE_IS_EDIT, false);

            if (isEdit) {
                tag_title = bundle.getString(BUNDLE_TAG_TITLE);
            }
        }

        getSupportActionBar().setTitle(tag_title);

        titleText = (TextView) findViewById(R.id.tagTitle);
        contentText = (EditText) findViewById(R.id.tagDescription);

        //set tag title
        JSONObject rb = new JSONObject();
        try {
            rb.put(FIELD_TITLE, tag_title);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addRequest(API_TAG_GET, rb, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                TagResponse tagResponse = new TagResponse(response);

                titleText.setText(tagResponse.tag_title);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, null);

        //If it is edit, pre-fill the view
        if (isEdit) {
            final JSONObject requestBody = new JSONObject();
            try {
                requestBody.put(FIELD_TITLE, tag_title);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            addRequest(API_TAG_GET, requestBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    TagResponse tagResponse = new TagResponse(response);

                    titleText.setText(tagResponse.tag_title);
                    contentText.setText(tagResponse.description);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }, null);
        }
    }


    private void clickSaveButton() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(FIELD_TITLE, titleText.getText());
            requestBody.put(FIELD_CONTENT, contentText.getText());

            if (isEdit)
                requestBody.put(FIELD_TITLE, tag_title);

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
                        mActivity.startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mActivity, new String(error.networkResponse.data), Toast.LENGTH_LONG).show();
                        //TODO ERROR HANDLING
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

}
