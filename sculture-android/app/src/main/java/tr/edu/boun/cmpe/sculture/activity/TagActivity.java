package tr.edu.boun.cmpe.sculture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import tr.edu.boun.cmpe.sculture.R;

import tr.edu.boun.cmpe.sculture.models.response.TagResponse;

import static tr.edu.boun.cmpe.sculture.Constants.API_TAG_GET;
import static tr.edu.boun.cmpe.sculture.Constants.BUNDLE_TAG_TITLE;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_TITLE;
import static tr.edu.boun.cmpe.sculture.Utils.addRequest;

public class TagActivity extends AppCompatActivity {
    TextView tagTitle;
    TextView tagDescription;
    Button createDescription;

    Bundle bundle;
    String tag_title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_description);
        bundle = getIntent().getExtras();
        if(bundle != null)
            tag_title = bundle.getString(BUNDLE_TAG_TITLE);

        Log.i("HERE", "" + tag_title);
        tagTitle = (TextView) findViewById(R.id.tag_title);
        tagDescription = (TextView) findViewById(R.id.tag_description);
        createDescription = (Button) findViewById(R.id.create_description);

        getTag(tag_title);

        if(tagDescription != null) {
            createDescription.setVisibility(View.GONE);
        } else {
            createDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), TagDescCreateActivity.class);
                    startActivity(i);
                }
            });
        }

    }

    private void getTag(String title) {
        final JSONObject requestObject = new JSONObject();
        try {
            requestObject.put(FIELD_TITLE, title);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        addRequest(API_TAG_GET, requestObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        TagResponse tag = new TagResponse(response);
                        tagTitle.setText(tag.tag_title);
                        tagDescription.setText(tag.description);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public  void onErrorResponse(VolleyError error) {

                    }
                }, null);
    }
}
