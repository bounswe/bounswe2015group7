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
    EditText titleText;
    EditText contentText;
    EditText tag1Text;
    EditText tag2Text;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_create);

        titleText = (EditText) findViewById(R.id.title);
        contentText = (EditText) findViewById(R.id.content);
        tag1Text = (EditText) findViewById(R.id.tag1);
        tag2Text = (EditText) findViewById(R.id.tag2);

        button = (Button) findViewById(R.id.postStorybutton);

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.postStorybutton:
                clickCreateButton();
                break;
        }
    }

    private void clickCreateButton() {
        String title = titleText.getText().toString();
        String content = contentText.getText().toString();
        String tag1 = tag1Text.getText().toString();
        String tag2 = tag2Text.getText().toString();
        String emptyTag = "NoTag";

        HashMap<String, Object> param = new HashMap<>();
        param.put("title", title);
        param.put("content", content);

        ArrayList<String> tags = new ArrayList<>();

        // if there is no tag, add "NoTag" value to tags
        if(tag1.isEmpty() && tag2.isEmpty()) {
            tags.add(emptyTag);
        } else {
            tags.add(tag1);
            tags.add(tag2);
        }

        param.put("tags", tags);

        ParseCloud.callFunctionInBackground("story_create", param, new FunctionCallback<HashMap>() {
                    @Override
                    public void done(HashMap response, ParseException e) {
                        if (e == null) {
                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                            titleText.setText("");
                            contentText.setText("");
                            tag1Text.setText("");
                            tag2Text.setText("");

                        } else
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
