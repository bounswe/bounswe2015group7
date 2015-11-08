package tr.edu.boun.cmpe.sculture.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchButton = (Button) findViewById(R.id.searchButton);
        searchText = (EditText) findViewById(R.id.searchEditText);
        resultText = (TextView) findViewById(R.id.searchResultText);
        searchButton.setOnClickListener(this);
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
                            for (HashMap item : list)
                                s += item.get("title") + "\n";

                            resultText.setText(s);

                        } else
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
