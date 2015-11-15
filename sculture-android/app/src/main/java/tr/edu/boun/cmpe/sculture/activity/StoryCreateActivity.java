package tr.edu.boun.cmpe.sculture.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

        //TODO Create story request

    }
}
