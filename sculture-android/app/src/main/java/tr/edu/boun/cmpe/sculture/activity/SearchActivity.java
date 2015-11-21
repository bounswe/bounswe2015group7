package tr.edu.boun.cmpe.sculture.activity;

import android.app.SearchManager;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.adapter.RecyclerViewAdapter;


public class SearchActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;

    int co = 0;
    JSONArray dataset = new JSONArray();

    //TODO DELETE THIS TESTING
    JSONArray jsonArray = new JSONArray();
    HashMap<Integer, JSONObject> stroies = new HashMap<>();

    //TODO DELETE THIS TESTING
    public void create_data() throws JSONException {
        for (int i = 0; i < 10; i++) {
            JSONObject object = new JSONObject();
            object.put("story_id", i);
            object.put("title", "my title " + i);
            object.put("owner_id", i + 1000);
            object.put("create_date", new Date().getTime() - i * 1000000);
            object.put("last_editor_id", i + 1000);
            object.put("last_edit_date", new Date().getTime() - i * 1000000);
            object.put("content", "My content is this bla bla bla " + i);
            JSONArray tags = new JSONArray();
            tags.put("tag1");
            tags.put("tag2");
            object.put("tags", tags);
            object.put("positive_vote", 10 + i);
            object.put("negative_vote", 1 + i);
            jsonArray.put(object);
            stroies.put(i, object);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            create_data();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_search);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

        mRecyclerViewAdapter = new RecyclerViewAdapter(dataset, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
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
                //TODO Search here
                try {
                    Log.i("HERE", jsonArray.length() + "");
                    dataset.put(jsonArray.getJSONObject(co++));
                    mRecyclerViewAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
