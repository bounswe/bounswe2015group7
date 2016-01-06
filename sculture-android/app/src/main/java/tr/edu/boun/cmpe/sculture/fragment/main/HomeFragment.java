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

import static tr.edu.boun.cmpe.sculture.Constants.API_RECOMMENDATION_SIMILIAR;
import static tr.edu.boun.cmpe.sculture.Constants.API_RECOMMENDATION_TRENDING;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_PAGE;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_SIZE;
import static tr.edu.boun.cmpe.sculture.Utils.addRequest;

public class HomeFragment extends Fragment {
    private RecyclerView story_list_recycler;
    LinearLayoutManager mLayoutManager;
    StoryListViewAdapter mStoryListViewAdapter;

    @SuppressWarnings("FieldCanBeLocal")
    private static int SIZE = 5;
    private int TRENDING_PAGE = 1;
    private int SIMILAR_PAGE = 1;
    private int QUEUE = 0;
    private boolean is_loading_more = false;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        story_list_recycler = (RecyclerView) view.findViewById(R.id.stories);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mStoryListViewAdapter = new StoryListViewAdapter(getActivity());
        story_list_recycler.setLayoutManager(mLayoutManager);
        story_list_recycler.setAdapter(mStoryListViewAdapter);

        setRecyclerListeners();

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
            TRENDING_PAGE = 1;
            SIMILAR_PAGE = 1;
            QUEUE = 0;
            is_loading_more = false;
            load_story();
        }
    }

    private void load_story() {
        int totalItemCount = mLayoutManager.getItemCount();
        int lastVisibleIndex = mLayoutManager.findLastVisibleItemPosition();

        boolean loadMore = lastVisibleIndex == totalItemCount - 1;


        if (loadMore && !is_loading_more) {
            is_loading_more = true;
            if (QUEUE % 2 == 1 && !BaseApplication.baseApplication.checkLogin())
                QUEUE++;
            JSONObject requestBody = new JSONObject();
            try {
                if (QUEUE % 2 == 0)
                    requestBody.put(FIELD_PAGE, TRENDING_PAGE++);
                else
                    requestBody.put(FIELD_PAGE, SIMILAR_PAGE++);
                requestBody.put(FIELD_SIZE, SIZE);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            final int q = QUEUE;
            String API;
            if (QUEUE % 2 == 0)
                API = API_RECOMMENDATION_TRENDING;
            else
                API = API_RECOMMENDATION_SIMILIAR;

            QUEUE++;
            addRequest(API, requestBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    SearchResponse searchResponse = new SearchResponse(response);

                    for (StoryResponse story : searchResponse.result)
                        mStoryListViewAdapter.addElement(story);

                    if (searchResponse.result.size() == 0) {
                        if (q % 2 == 0)
                            TRENDING_PAGE--;
                        else
                            SIMILAR_PAGE--;
                    }

                    is_loading_more = false;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ErrorResponse errorResponse = new ErrorResponse(error);
                    Toast.makeText(getActivity(), R.string.error_occurred, Toast.LENGTH_SHORT).show();
                    Log.e("USER PROFILE", errorResponse.toString());
                }
            }, null);
        }

    }

}
