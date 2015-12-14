package tr.edu.boun.cmpe.sculture.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.adapter.StoryListViewAdapter;
import tr.edu.boun.cmpe.sculture.models.response.BaseStoryResponse;
import tr.edu.boun.cmpe.sculture.models.response.LoginResponse;
import tr.edu.boun.cmpe.sculture.models.response.SearchResponse;

import static tr.edu.boun.cmpe.sculture.BaseApplication.baseApplication;
import static tr.edu.boun.cmpe.sculture.Constants.API_USER_GET;
import static tr.edu.boun.cmpe.sculture.Constants.API_USER_STORIES;
import static tr.edu.boun.cmpe.sculture.Constants.BUNDLE_VISITED_USER_ID;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_ID;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_PAGE;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_SIZE;
import static tr.edu.boun.cmpe.sculture.Utils.addRequest;

public class ProfilePageActivity extends AppCompatActivity {

    RecyclerView story_list_recycler;
    Button follow;
    ActionBar actionBar;
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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            visitedUserID = bundle.getLong(BUNDLE_VISITED_USER_ID);
        actionBar = getSupportActionBar();

        currentUserID = baseApplication.getUSER_ID();

        follow = (Button) findViewById(R.id.follow);

        story_list_recycler = (RecyclerView) findViewById(R.id.profile_story_list);
        mLayoutManager = new LinearLayoutManager(this);
        mStoryListViewAdapter = new StoryListViewAdapter(this);
        story_list_recycler.setLayoutManager(mLayoutManager);
        story_list_recycler.setAdapter(mStoryListViewAdapter);

        setRecyclerListeners();
        loadUser();
        load_story();

        if (currentUserID == visitedUserID)
            follow.setVisibility(View.GONE);

        //TODO Follow unfollow
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followUser();
            }
        });

    }

    private void loadUser() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(FIELD_ID, BUNDLE_VISITED_USER_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addRequest(API_USER_GET, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LoginResponse loginResponse = new LoginResponse(response);
                visitedUserID = loginResponse.id;
                setActionBarTitle(loginResponse.username);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO error handling
            }
        }, null);
    }

    private void followUser() {

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

    private void setActionBarTitle(String s) {
        if (actionBar != null)
            actionBar.setTitle(s);
    }
}
