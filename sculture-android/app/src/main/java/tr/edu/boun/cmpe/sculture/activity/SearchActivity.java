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
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.HashMap;

import tr.edu.boun.cmpe.sculture.R;


public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    EditText searchText;
    TextView resultText;
    Button searchButton;
    RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    ArrayList<ArrayList<String>> myDataset = new ArrayList<ArrayList<String>>();
    ArrayList<String> story = new ArrayList<String>();
    private RecyclerViewAdaptor mRecyclerViewAdaptor;


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

        HashMap<String, Object> param = new HashMap<>();
        param.put("query", query);
        param.put("size", 10);
        param.put("page", 0);

        ParseCloud.callFunctionInBackground("search", param, new FunctionCallback<ArrayList<HashMap>>() {
                    @Override
                    public void done(ArrayList<HashMap> list, ParseException e) {
                        if (e == null) {
                            String s = "";
                            myDataset.clear();
                            for (HashMap item : list) {

                                //s += item.get("title") + "\n";
                                ArrayList <String> story = new ArrayList<String>();
                                story.add((String) item.get("title"));
                                //story.add("Tags: "/*(String) item.get("tags")*/);
                                //story.add("Content: "+(String) item.get("content"));
                                //ParseUser usr = (ParseUser)item.get("ownerId");
                                //story.add("ownerId: "+(String)item.get("ownerId").toString());
                                //story.add("created at: "+(String) item.get("createdAt"));
                                //story.add("LastEditor ID: "+(String) item.get("lastEditorId").toString());
                                story.add((String) item.get("updatedAt"));
                                story.add((String) item.get("id"));
                                myDataset.add(story);
                            }
                            mRecyclerViewAdaptor = new RecyclerViewAdaptor(myDataset , SearchActivity.this);

                            mRecyclerView.setAdapter(mRecyclerViewAdaptor);
                            resultText.setText(s);
                        } else
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    public void sendToNextActivity(String id) {
        Intent intent = new Intent(this, StoryShowActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
