package tr.edu.boun.cmpe.sculture.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.adapter.StoryListViewAdapter;
import tr.edu.boun.cmpe.sculture.fragment.main.HomeFragment;
import tr.edu.boun.cmpe.sculture.fragment.main.ProfileFragment;

import static tr.edu.boun.cmpe.sculture.Constants.API_SEARCH;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_QUERY;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_RESULTS;
import static tr.edu.boun.cmpe.sculture.Constants.REQUEST_TAG_SEARCH;
import static tr.edu.boun.cmpe.sculture.Utils.addRequest;
import static tr.edu.boun.cmpe.sculture.Utils.removeRequests;
import static tr.edu.boun.cmpe.sculture.BaseApplication.baseApplication;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private RecyclerView recyclerView;
    private StoryListViewAdapter mStoryListViewAdapter;

    private FloatingActionButton createStoryButton;
    private MenuItem logout_menu_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        //Search
        recyclerView = (RecyclerView) findViewById(R.id.search_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mStoryListViewAdapter = new StoryListViewAdapter(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mStoryListViewAdapter);

        createStoryButton = (FloatingActionButton) findViewById(R.id.createStoryButton);
        createStoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), StoryCreateActivity.class);
                startActivity(i);
            }
        });

    }

    public void onResume() {
        super.onResume();
        if (!baseApplication.checkLogin()) {
            createStoryButton.setVisibility(View.GONE);
        } else {
            createStoryButton.setVisibility(View.VISIBLE);
        }

        if (logout_menu_item != null)
            logout_menu_item.setVisible(baseApplication.checkLogin());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);


        logout_menu_item = menu.findItem(R.id.action_logout);
        logout_menu_item.setVisible(baseApplication.checkLogin());

        MenuItem search_menu_item = menu.findItem(R.id.action_search);
        MenuItemCompat.setOnActionExpandListener(search_menu_item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                mViewPager.setVisibility(View.GONE);
                mTabLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mViewPager.setVisibility(View.VISIBLE);
                mTabLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                return true;
            }
        });


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
                                    mStoryListViewAdapter.clearElements();
                                    JSONArray array = response.getJSONArray(FIELD_RESULTS);
                                    for (int i = 0; i < array.length(); i++)
                                        mStoryListViewAdapter.addElement(array.getJSONObject(i));
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, R.string.action_settings, Toast.LENGTH_LONG).show();
                break;
            case R.id.action_logout:
                baseApplication.logOut();
                this.recreate();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new HomeFragment();
                case 1:
                    return new ProfileFragment();
                default:
                    return new HomeFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab_title_home);
                case 1:
                    return getString(R.string.tab_title_profile);
                default:
                    return getString(R.string.tab_title_home);

            }
        }
    }
}