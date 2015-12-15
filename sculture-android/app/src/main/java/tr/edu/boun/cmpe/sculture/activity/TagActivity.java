package tr.edu.boun.cmpe.sculture.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.adapter.StoryListViewAdapter;
import tr.edu.boun.cmpe.sculture.models.response.StoryResponse;
import tr.edu.boun.cmpe.sculture.models.response.SearchResponse;
import tr.edu.boun.cmpe.sculture.models.response.TagResponse;

import static tr.edu.boun.cmpe.sculture.Constants.API_SEARCH;
import static tr.edu.boun.cmpe.sculture.Constants.API_TAG_GET;
import static tr.edu.boun.cmpe.sculture.Constants.BUNDLE_TAG_TITLE;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_PAGE;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_QUERY;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_SIZE;
import static tr.edu.boun.cmpe.sculture.Constants.REQUEST_TAG_SEARCH;
import static tr.edu.boun.cmpe.sculture.Utils.addRequest;

public class TagActivity extends AppCompatActivity {
    private static final int SIZE = 10;
    private TextView tagDescription;
    private TextView update_time;
    private ActionBar actionBar;
    private RecyclerView story_list_recycler;

    LinearLayoutManager mLayoutManager;
    StoryListViewAdapter mStoryListViewAdapter;

    private int PAGE = 1;
    private boolean is_loading_more = false;
    private boolean is_reach_end = false;

    private Activity mActivity;


    String tag_title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            tag_title = bundle.getString(BUNDLE_TAG_TITLE);

        actionBar = getSupportActionBar();

        setActionBarTitle(tag_title);

        tagDescription = (TextView) findViewById(R.id.tag_description);
        update_time = (TextView) findViewById(R.id.tag_update);

        story_list_recycler = (RecyclerView) findViewById(R.id.tag_story_list);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mStoryListViewAdapter = new StoryListViewAdapter(this);
        story_list_recycler.setLayoutManager(mLayoutManager);
        story_list_recycler.setAdapter(mStoryListViewAdapter);

        setRecyclerListeners();

        loadTag();

        load_story();
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
                tagDescription.setText(tagResponse.description);
                setActionBarTitle(tagResponse.tag_title);
                set_visibilities();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                set_visibilities();
            }
        }, null);
    }

    private void set_visibilities() {
        String s = tagDescription.getText().toString();
        if (s.equals("")) {
            update_time.setVisibility(View.GONE);
            tagDescription.setVisibility(View.GONE);
        } else {
            update_time.setVisibility(View.VISIBLE);
            tagDescription.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.tag_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent(getApplicationContext(), TagDescCreateActivity.class);
                intent.putExtra(BUNDLE_TAG_TITLE, tag_title);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
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
        final JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(FIELD_QUERY, tag_title);
            requestBody.put(FIELD_SIZE, SIZE);
            requestBody.put(FIELD_PAGE, PAGE);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        int totalItemCount = mLayoutManager.getItemCount();
        int lastVisibleIndex = mLayoutManager.findLastVisibleItemPosition();


        boolean loadMore = lastVisibleIndex == totalItemCount - 1;


        if ((loadMore && !is_loading_more && !is_reach_end) || PAGE == 1) {
            PAGE++;
            is_loading_more = true;
            addRequest(API_SEARCH, requestBody, new Response.Listener<JSONObject>() {
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

                }
            }, REQUEST_TAG_SEARCH);
        }

    }

    private void setActionBarTitle(String s) {
        if (actionBar != null)
            actionBar.setTitle(s);
    }
}
