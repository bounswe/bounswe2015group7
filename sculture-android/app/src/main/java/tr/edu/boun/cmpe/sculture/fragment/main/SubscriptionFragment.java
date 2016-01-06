package tr.edu.boun.cmpe.sculture.fragment.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import tr.edu.boun.cmpe.sculture.BaseApplication;
import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.adapter.StoryListViewAdapter;
import tr.edu.boun.cmpe.sculture.models.response.ErrorResponse;
import tr.edu.boun.cmpe.sculture.models.response.SearchResponse;
import tr.edu.boun.cmpe.sculture.models.response.StoryResponse;

import static tr.edu.boun.cmpe.sculture.Constants.API_RECOMMENDATION_FOLLOW;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_PAGE;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_SIZE;
import static tr.edu.boun.cmpe.sculture.Utils.addRequest;

public class SubscriptionFragment extends Fragment {


    LinearLayoutManager mLayoutManager;
    StoryListViewAdapter mStoryListViewAdapter;
    private RecyclerView story_list_recycler;
    private int PAGE = 1;
    private boolean is_loading_more = false;
    private boolean is_reach_end = false;

    public SubscriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscription, container, false);
        story_list_recycler = (RecyclerView) view.findViewById(R.id.subscription_stories);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mStoryListViewAdapter = new StoryListViewAdapter(getActivity());
        story_list_recycler.setLayoutManager(mLayoutManager);
        story_list_recycler.setAdapter(mStoryListViewAdapter);

        setRecyclerListeners();

        load_story();
        return view;
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

    public void onStart() {
        super.onStart();
        if (mStoryListViewAdapter.getItemCount() == 0) {
            PAGE = 1;
            is_loading_more = false;
            is_reach_end = false;
            load_story();
        }
    }

    private void load_story() {
        if (!BaseApplication.baseApplication.checkLogin()) {
            Toast.makeText(getActivity(), R.string.loginAdvice, Toast.LENGTH_SHORT).show();
            return;
        }
        int totalItemCount = mLayoutManager.getItemCount();
        int lastVisibleIndex = mLayoutManager.findLastVisibleItemPosition();

        boolean loadMore = lastVisibleIndex == totalItemCount - 1;

        if ((loadMore && !is_loading_more && !is_reach_end) || PAGE == 1) {
            is_loading_more = true;
            JSONObject requestBody = new JSONObject();
            try {
                requestBody.put(FIELD_PAGE, PAGE);
                requestBody.put(FIELD_SIZE, 10);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PAGE++;
            addRequest(API_RECOMMENDATION_FOLLOW, requestBody, new Response.Listener<JSONObject>() {
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
                    Toast.makeText(getActivity(), R.string.error_occurred, Toast.LENGTH_SHORT).show();
                    Log.e("SUBSCRIPTIONS", errorResponse.toString());
                }
            }, null);
        }

    }
}
