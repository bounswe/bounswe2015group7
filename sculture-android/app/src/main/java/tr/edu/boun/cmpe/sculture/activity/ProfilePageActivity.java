package tr.edu.boun.cmpe.sculture.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.adapter.StoryListViewAdapter;
import tr.edu.boun.cmpe.sculture.models.response.BaseStoryResponse;
import tr.edu.boun.cmpe.sculture.models.response.SearchResponse;

import static tr.edu.boun.cmpe.sculture.BaseApplication.baseApplication;
import static tr.edu.boun.cmpe.sculture.Constants.API_USER_FOLLOW;
import static tr.edu.boun.cmpe.sculture.Constants.API_USER_GET;
import static tr.edu.boun.cmpe.sculture.Constants.API_USER_STORIES;
import static tr.edu.boun.cmpe.sculture.Constants.BUNDLE_VISITED_USER_ID;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_ID;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_PAGE;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_SIZE;
import static tr.edu.boun.cmpe.sculture.Constants.PREF_USER_ID;
import static tr.edu.boun.cmpe.sculture.Utils.addRequest;

public class ProfilePageActivity extends AppCompatActivity {

    TextView username;
    TextView email;
    RecyclerView story_list_recycler;
    RelativeLayout loggedInLayout;
    Button follow;

    LinearLayoutManager mLayoutManager;
    StoryListViewAdapter mStoryListViewAdapter;

    private int PAGE = 1;
    private boolean is_loading_more = false;
    private boolean is_reach_end = false;

    long currentUserID;
    long visitedUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);


        loggedInLayout = (RelativeLayout) findViewById(R.id.profile_layout_loggedIn);

        username = (TextView) findViewById(R.id.profile_username);
        email = (TextView) findViewById(R.id.profile_email);

        follow = (Button) findViewById(R.id.follow);

        story_list_recycler = (RecyclerView) findViewById(R.id.profile_story_list);
        mLayoutManager = new LinearLayoutManager(this);
        mStoryListViewAdapter = new StoryListViewAdapter(this);
        story_list_recycler.setLayoutManager(mLayoutManager);
        story_list_recycler.setAdapter(mStoryListViewAdapter);

        setRecyclerListeners();

        currentUserID = baseApplication.getUSER_ID();

        JSONObject requestBody = new JSONObject();
        try {
           requestBody.put(FIELD_ID, BUNDLE_VISITED_USER_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addRequest(API_USER_GET, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    visitedUserID = response.getLong(BUNDLE_VISITED_USER_ID);
                    username.setText(baseApplication.getUSERNAME());
                    email.setText(baseApplication.getEMAIL());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO error handling
            }
        }, null);

        load_story();

        if(currentUserID == visitedUserID)
            follow.setVisibility(View.GONE);

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject rb = new JSONObject();
                try {
                    rb.put(API_USER_FOLLOW, visitedUserID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void setRecyclerListeners() {
        story_list_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                load_story();
            }
        });
    }

    private void load_story() {

        int totalItemCount = mLayoutManager.getItemCount();
        int lastVisibleIndex = mLayoutManager.findLastVisibleItemPosition();

        boolean loadMore = lastVisibleIndex == totalItemCount - 1;

        if ((loadMore && !is_loading_more && !is_reach_end) || PAGE == 1) {
            is_loading_more = true;
            JSONObject requestBody = new JSONObject();
            try {
                requestBody.put(FIELD_ID, visitedUserID);
                requestBody.put(FIELD_PAGE, PAGE);
                requestBody.put(FIELD_SIZE, 10);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PAGE++;
            addRequest(API_USER_STORIES, requestBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    SearchResponse searchResponse = new SearchResponse(response);

                    for (BaseStoryResponse story : searchResponse.result)
                        mStoryListViewAdapter.addElement(story);

                    if (searchResponse.result.size() == 0)
                        is_reach_end = true;

                    is_loading_more = false;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }, null);
        }

    }
}
