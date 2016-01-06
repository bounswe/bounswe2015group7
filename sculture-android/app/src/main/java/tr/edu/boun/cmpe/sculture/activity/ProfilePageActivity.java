package tr.edu.boun.cmpe.sculture.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.adapter.StoryListViewAdapter;
import tr.edu.boun.cmpe.sculture.models.response.ErrorResponse;
import tr.edu.boun.cmpe.sculture.models.response.SearchResponse;
import tr.edu.boun.cmpe.sculture.models.response.StoryResponse;
import tr.edu.boun.cmpe.sculture.models.response.UserFollowResponse;
import tr.edu.boun.cmpe.sculture.models.response.UserGetResponse;

import static tr.edu.boun.cmpe.sculture.BaseApplication.baseApplication;
import static tr.edu.boun.cmpe.sculture.Constants.API_USER_FOLLOW;
import static tr.edu.boun.cmpe.sculture.Constants.API_USER_GET;
import static tr.edu.boun.cmpe.sculture.Constants.API_USER_STORIES;
import static tr.edu.boun.cmpe.sculture.Constants.BUNDLE_VISITED_USER_ID;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_ID;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_PAGE;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_SIZE;
import static tr.edu.boun.cmpe.sculture.Utils.addRequest;

/**
 * User profile viewing screen
 * <pre></pre>
 * {@link tr.edu.boun.cmpe.sculture.Constants#BUNDLE_VISITED_USER_ID}: User ID of the visited user.
 */
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

    boolean is_followed = false;

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
        final JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("userId", visitedUserID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addRequest(API_USER_GET, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                UserGetResponse userGetResponse = new UserGetResponse(response);
                visitedUserID = userGetResponse.user_id;
                is_followed = userGetResponse.is_following;
                refreshFollowButton();
                setActionBarTitle(userGetResponse.username);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorResponse errorResponse = new ErrorResponse(error);
                Toast.makeText(getApplicationContext(), R.string.error_occurred, Toast.LENGTH_SHORT).show();
                Log.e("PROFILE GET", errorResponse.toString());
            }
        }, null);
    }

    private void followUser() {
        final JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("user_id", visitedUserID);
            requestBody.put("is_follow", !is_followed);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        addRequest(API_USER_FOLLOW, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                UserFollowResponse userFollowResponse = new UserFollowResponse(response);
                is_followed = userFollowResponse.is_follow;
                refreshFollowButton();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorResponse errorResponse = new ErrorResponse(error);
                Toast.makeText(getApplicationContext(), R.string.error_occurred, Toast.LENGTH_SHORT).show();
                Log.e("PROFILE FOLLOW", errorResponse.toString());
            }
        }, null);
    }


    private void refreshFollowButton() {
        if (is_followed)
            follow.setText(R.string.unfollow);
        else
            follow.setText(R.string.follow);
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

                    for (StoryResponse story : searchResponse.result)
                        mStoryListViewAdapter.addElement(story);

                    if (searchResponse.result.size() == 0)
                        is_reach_end = true;

                    is_loading_more = false;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ErrorResponse errorResponse = new ErrorResponse(error);
                    Toast.makeText(getApplicationContext(), R.string.error_occurred, Toast.LENGTH_SHORT).show();
                    Log.e("PROFILE STORIES", errorResponse.toString());
                }
            }, null);
        }

    }

    private void setActionBarTitle(String s) {
        if (actionBar != null)
            actionBar.setTitle(s);
    }
}
