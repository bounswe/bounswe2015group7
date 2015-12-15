package tr.edu.boun.cmpe.sculture.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.adapter.StoryListViewAdapter;
import tr.edu.boun.cmpe.sculture.fragment.main.HomeFragment;
import tr.edu.boun.cmpe.sculture.fragment.main.ProfileFragment;
import tr.edu.boun.cmpe.sculture.models.response.BaseStoryResponse;
import tr.edu.boun.cmpe.sculture.models.response.ErrorResponse;
import tr.edu.boun.cmpe.sculture.models.response.SearchResponse;

import static tr.edu.boun.cmpe.sculture.BaseApplication.baseApplication;
import static tr.edu.boun.cmpe.sculture.Constants.API_SEARCH;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_PAGE;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_QUERY;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_SIZE;
import static tr.edu.boun.cmpe.sculture.Constants.REQUEST_TAG_SEARCH;
import static tr.edu.boun.cmpe.sculture.Utils.addRequest;
import static tr.edu.boun.cmpe.sculture.Utils.removeRequests;

public class MainActivity extends AppCompatActivity {

    private static final int SIZE = 10;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private RecyclerView recyclerView;
    private StoryListViewAdapter mStoryListViewAdapter;
    private LinearLayoutManager mLayoutManager;
    private FloatingActionButton createStoryButton;
    private MenuItem logout_menu_item;
    private MainActivity mActivity;
    private int PAGE = 1;
    private boolean is_loading_more = false;
    private boolean is_reach_end = false;


    private String search_query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
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
        mLayoutManager = new LinearLayoutManager(this);
        mStoryListViewAdapter = new StoryListViewAdapter(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mStoryListViewAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                load_story();
            }
        });

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
                is_loading_more = false;
                is_reach_end = false;
                PAGE = 1;
                removeRequests(REQUEST_TAG_SEARCH);
                mStoryListViewAdapter.clearElements();
                search_query = query;
                load_story();
                return true;
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

    private void load_story() {
        final JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(FIELD_QUERY, search_query);
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
                    for (BaseStoryResponse story : searchResponse.result)
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
                    Log.e("SEARCH", errorResponse.toString());


                }
            }, REQUEST_TAG_SEARCH);
        }

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
            int id;
            switch (position) {
                case 0:
                    id = R.drawable.ic_home_white_48dp;
                    break;
                case 1:
                    id = R.drawable.ic_person_white_48dp;
                    break;
                default:
                    id = R.drawable.ic_home_white_48dp;
                    break;
            }

            Drawable image = ContextCompat.getDrawable(mActivity, id);
            image.setBounds(0, 0, (int) (image.getIntrinsicWidth() / 1.5), (int) (image.getIntrinsicHeight() / 1.5));
            SpannableString sb = new SpannableString(" ");
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
        }
    }
}