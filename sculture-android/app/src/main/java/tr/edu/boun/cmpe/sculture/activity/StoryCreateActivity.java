package tr.edu.boun.cmpe.sculture.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.HashMap;

import tr.edu.boun.cmpe.sculture.R;


public class StoryCreateActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editText2;
    EditText editText3;
    EditText editText4;
    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_create);

        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);
        editText4 = (EditText) findViewById(R.id.editText4);
        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                clickSearchButton();
                break;
        }
    }

    private void clickSearchButton() {
        String title = editText.getText().toString();
        String content = editText2.getText().toString();
        String tag1 = editText3.getText().toString();
        String tag2 = editText4.getText().toString();

        HashMap<String, Object> param = new HashMap<>();
        param.put("title", title);
        param.put("content", content);

        ArrayList<String> tags = new ArrayList<>();
        tags.add(tag1);
        tags.add(tag2);

        param.put("tags", tags);

        ParseCloud.callFunctionInBackground("story_create", param, new FunctionCallback<HashMap>() {
                    @Override
                    public void done(HashMap response, ParseException e) {
                        if (e == null) {
                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                            editText.setText("");
                            editText2.setText("");
                            editText3.setText("");
                            editText4.setText("");

                        } else
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
