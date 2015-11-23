package tr.edu.boun.cmpe.sculture.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tr.edu.boun.cmpe.sculture.R;

import static tr.edu.boun.cmpe.sculture.Constants.*;

import static tr.edu.boun.cmpe.sculture.Utils.*;

public class StoryCreateActivity extends AppCompatActivity implements View.OnClickListener {
    EditText titleText;
    EditText contentText;
    EditText tag1Text;
    EditText tag2Text;
    Button button;
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_story_create);

        titleText = (EditText) findViewById(R.id.title);
        contentText = (EditText) findViewById(R.id.content);
        tag1Text = (EditText) findViewById(R.id.tag1);
        tag2Text = (EditText) findViewById(R.id.tag2);

        button = (Button) findViewById(R.id.postStorybutton);

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.postStorybutton:
                clickCreateButton();
                break;
        }
    }

    private void clickCreateButton() {
        String title = titleText.getText().toString();
        String content = contentText.getText().toString();
        String tag1 = tag1Text.getText().toString();
        String tag2 = tag2Text.getText().toString();


        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(FIELD_TITLE, title);
            requestBody.put(FIELD_CONTENT, content);
            JSONArray tags = new JSONArray();
            tags.put(tag1);
            tags.put(tag2);
            requestBody.put(FIELD_TAGS, tags);
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
}
