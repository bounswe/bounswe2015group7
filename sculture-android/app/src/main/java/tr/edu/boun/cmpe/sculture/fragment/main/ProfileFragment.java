package tr.edu.boun.cmpe.sculture.fragment.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.activity.LoginRegistrationActivity;
import tr.edu.boun.cmpe.sculture.adapter.StoryListViewAdapter;

import static tr.edu.boun.cmpe.sculture.BaseApplication.baseApplication;
import static tr.edu.boun.cmpe.sculture.Constants.API_USER_STORIES;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_ID;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_PAGE;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_RESULTS;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_SIZE;
import static tr.edu.boun.cmpe.sculture.Utils.addRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private TextView username;
    private TextView email;
    private RecyclerView story_list_recycler;
    private RelativeLayout loggedInLayout;
    private RelativeLayout loggedOutLayout;

    LinearLayoutManager mLayoutManager;
    StoryListViewAdapter mStoryListViewAdapter;

    private int PAGE = 1;
    private boolean is_loading_more = false;
    private boolean is_reach_end = false;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        loggedInLayout = (RelativeLayout) view.findViewById(R.id.profile_layout_loggedIn);
        loggedOutLayout = (RelativeLayout) view.findViewById(R.id.profile_layout_loggedOut);

        username = (TextView) view.findViewById(R.id.profile_username);
        email = (TextView) view.findViewById(R.id.profile_email);


        story_list_recycler = (RecyclerView) view.findViewById(R.id.profile_story_list);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mStoryListViewAdapter = new StoryListViewAdapter(getActivity());
        story_list_recycler.setLayoutManager(mLayoutManager);
        story_list_recycler.setAdapter(mStoryListViewAdapter);

        setRecyclerListeners();

        load_story();
        Button login_register_button = (Button) view.findViewById(R.id.login_register);
        login_register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginRegistrationActivity.class);
                startActivity(intent);
            }
        });


        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!baseApplication.checkLogin()) {
            loggedOutLayout.setVisibility(View.VISIBLE);
            loggedInLayout.setVisibility(View.GONE);
        } else {
            loggedOutLayout.setVisibility(View.GONE);
            loggedInLayout.setVisibility(View.VISIBLE);


            username.setText(baseApplication.getUSERNAME());
            email.setText(baseApplication.getEMAIL());
        }

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

        if (is_reach_end)
            Log.i("HERE", "HERE");
        if ((loadMore && !is_loading_more && !is_reach_end) || PAGE == 1) {
            is_loading_more = true;
            JSONObject requestBody = new JSONObject();
            try {
                requestBody.put(FIELD_ID, baseApplication.getUSER_ID());
                requestBody.put(FIELD_PAGE, PAGE);
                requestBody.put(FIELD_SIZE, 10);
                Log.i("HEREd", "" + baseApplication.getUSER_ID());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PAGE++;
            addRequest(API_USER_STORIES, requestBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JSONArray array;
                    try {
                        array = response.getJSONArray(FIELD_RESULTS);
                        for (int i = 0; i < array.length(); i++)
                            mStoryListViewAdapter.addElement(array.getJSONObject(i));
                        if (array.length() == 0)
                            is_reach_end = true;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
