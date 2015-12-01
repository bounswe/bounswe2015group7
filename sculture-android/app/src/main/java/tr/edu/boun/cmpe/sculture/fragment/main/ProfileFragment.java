package tr.edu.boun.cmpe.sculture.fragment.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.activity.LoginRegistrationActivity;
import tr.edu.boun.cmpe.sculture.adapter.StoryListViewAdapter;

import static tr.edu.boun.cmpe.sculture.BaseApplication.baseApplication;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    //TODO DELETE THIS AFTER REST CONNECTION
    private final ArrayList<JSONObject> stories = new ArrayList<>();
    private TextView username;
    private TextView email;
    private RecyclerView story_list_recycler;
    private RelativeLayout loggedInLayout;
    private RelativeLayout loggedOutLayout;

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
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        StoryListViewAdapter mStoryListViewAdapter = new StoryListViewAdapter(getActivity());
        story_list_recycler.setLayoutManager(mLayoutManager);
        story_list_recycler.setAdapter(mStoryListViewAdapter);


        //TODO DELETE AFTER REST CONNECTION
        for (int i = 0; i < 20; i++) {
            try {
                JSONObject jsonObject = new JSONObject("{\n" +
                        "  \"id\": 1,\n" +
                        "  \"title\": \"das" + i + "\",\n" +
                        "  \"creation_date\": 1448236800000,\n" +
                        "  \"update_date\": 1448236800000,\n" +
                        "  \"last_editor\": {\n" +
                        "    \"id\": 1,\n" +
                        "    \"username\": \"john\"\n" +
                        "  },\n" +
                        "  \"owner\": {\n" +
                        "    \"id\": 1,\n" +
                        "    \"username\": \"john\"\n" +
                        "  },\n" +
                        "  \"tags\": [\n" +
                        "    \"tag1\",\n" +
                        "    \"tag2\"\n" +
                        "  ],\n" +
                        "  \"positive_vote\": 0,\n" +
                        "  \"negative_vote\": 0,\n" +
                        "  \"report_count\": 0,\n" +
                        "  \"content\": \"content\"\n" +
                        "}");
                stories.add(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        setRecyclerListeners();
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
            LinearLayoutManager layoutManager;
            StoryListViewAdapter adapter;
            private boolean is_loading_more = false;
            private boolean is_reach_end = false;
            private int page = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                adapter = (StoryListViewAdapter) recyclerView.getAdapter();

                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleIndex = layoutManager.findLastVisibleItemPosition();


                boolean loadMore = lastVisibleIndex == totalItemCount - 1;

                //loading is used to see if its already loading, you have to manually manipulate this boolean variable
                if (loadMore && !is_loading_more && !is_reach_end) {
                    //TODO connect REST here
                    for (int i = page * 5; i < (page + 1) * 5; i++) {
                        if (i == stories.size()) {
                            is_reach_end = true;
                            break;
                        }
                        adapter.addElement(stories.get(i));
                    }
                    if (!is_reach_end)
                        page++;
                }
            }
        });
    }

}
