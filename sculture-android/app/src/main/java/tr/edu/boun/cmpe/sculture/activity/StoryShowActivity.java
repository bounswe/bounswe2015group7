package tr.edu.boun.cmpe.sculture.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import tr.edu.boun.cmpe.sculture.R;

public class StoryShowActivity extends AppCompatActivity {

    TextView title;
    TextView content;
    TextView tags;
    TextView storyOwner;
    TextView createdAt;
    TextView lastEditor;
    TextView lastUpdatedAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_show);
        Bundle bundle = getIntent().getExtras();
        title = (TextView) findViewById(R.id.storyTitle);
        content = (TextView) findViewById(R.id.storyContent);
        tags = (TextView) findViewById(R.id.storyTags);
        storyOwner = (TextView) findViewById(R.id.storyOwner);
        createdAt = (TextView) findViewById(R.id.storyCreatedAt);
        lastEditor = (TextView) findViewById(R.id.storyLastEditor);
        lastUpdatedAt = (TextView) findViewById(R.id.storyLastUpdatedAt);
        if(bundle.getString("id")!= null)
        {
            getStory(bundle.getString("id"));
        }

    }


    public void getStory(String id){
        HashMap<String, Object> param = new HashMap<>();

        param.put("id", id);

        ParseCloud.callFunctionInBackground("story_get", param, new FunctionCallback<HashMap>() {

                    @Override
                    public void done(HashMap item, ParseException e) {
                        if (e == null) {


                                //s += item.get("title") + "\n";
                                ArrayList<String> story = new ArrayList<String>();
                                title.setText((String) item.get("title"));
                            ArrayList<String> tags2 = (ArrayList<String> ) item.get("tags");
                            String all = "";
                            for(String tagsAll: tags2){
                                all+= tagsAll+" ";
                            }
                            //Toast.makeText(getApplicationContext(), tags.get(0), Toast.LENGTH_LONG).show();
                                //story.add("Tags: "+all);
                                tags.setText(all);
                                content.setText(""+(String) item.get("content"));
                                //ParseUser ps = (ParseUser) item.get("ownerId");
                                storyOwner.setText("Story Owner: "+(String)item.get("ownerId"));//ps.getObjectId());
                            String cr = ""+item.get("createdAt");
                                createdAt.setText("Story creation Time: "+cr.substring(0,10)+" "+ cr.substring(11,19));
                            cr = ""+item.get("updatedAt");
                                lastEditor.setText("Last Editor:"+(String) item.get("lastEditorId").toString());
                                lastUpdatedAt.setText("Last Update Time: "+cr.substring(0,10)+" "+ cr.substring(11,19));
                                //ParseUser usr = (ParseUser)item.get("ownerId");



                        } else
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    public String StringToDate(String ISODate){
        String dtStart = "ISODate";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date date = new Date();
        try {
            date = format.parse(dtStart);
            System.out.println(date);
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.toString();
    }

}
