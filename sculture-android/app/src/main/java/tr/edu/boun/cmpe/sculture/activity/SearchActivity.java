package tr.edu.boun.cmpe.sculture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.adapter.RecyclerViewAdapter;


public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    EditText searchText;
    TextView resultText;
    Button searchButton;
    RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    ArrayList<ArrayList<String>> myDataset = new ArrayList<ArrayList<String>>();
    ArrayList<String> story = new ArrayList<String>();
    private RecyclerViewAdapter mRecyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchButton = (Button) findViewById(R.id.searchButton);
        searchText = (EditText) findViewById(R.id.searchEditText);
        resultText = (TextView) findViewById(R.id.searchResultText);
        searchButton.setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchButton:
                clickSearchButton();
                break;
        }
    }

    private void clickSearchButton() {
        String query = searchText.getText().toString();
        //TODO Search request

    }

    public void sendToNextActivity(String id) {
        Intent intent = new Intent(this, StoryShowActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
