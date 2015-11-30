package tr.edu.boun.cmpe.sculture.activity;

import android.app.SearchManager;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.adapter.StoryListViewAdapter;

import static tr.edu.boun.cmpe.sculture.Constants.API_SEARCH;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_QUERY;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_RESULTS;
import static tr.edu.boun.cmpe.sculture.Constants.REQUEST_TAG_SEARCH;
import static tr.edu.boun.cmpe.sculture.Utils.addRequest;
import static tr.edu.boun.cmpe.sculture.Utils.removeRequests;

public class SearchActivity extends AppCompatActivity {

    private StoryListViewAdapter mStoryListViewAdapter;
    private JSONArray searchResults = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

        mStoryListViewAdapter = new StoryListViewAdapter(searchResults, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mStoryListViewAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //TODO RecyclerView is terrible. It should be updated.
                removeRequests(REQUEST_TAG_SEARCH);

                final JSONObject requestBody = new JSONObject();
                try {
                    requestBody.put(FIELD_QUERY, searchView.getQuery().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                addRequest(API_SEARCH, requestBody, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray array = response.getJSONArray(FIELD_RESULTS);
                                    for (int i = 0; i < array.length(); i++)
                                        searchResults.put(array.get(i));
                                    mStoryListViewAdapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }, REQUEST_TAG_SEARCH);


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }


}
