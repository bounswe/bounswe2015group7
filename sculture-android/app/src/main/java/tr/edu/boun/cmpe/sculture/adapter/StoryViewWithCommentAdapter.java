package tr.edu.boun.cmpe.sculture.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.Utils;
import tr.edu.boun.cmpe.sculture.activity.StoryShowActivity;

public class StoryViewWithCommentAdapter extends RecyclerView.Adapter<StoryViewWithCommentAdapter.ViewHolder> {
    private Activity mActivity;
    private final ArrayList<JSONObject> comments = new ArrayList<>();
    private long user_id;
    //private JSONArray comments;

    public StoryViewWithCommentAdapter(StoryShowActivity activity) {
        this.mActivity = activity;
    }


    public void addElement(JSONObject story) {
        comments.add(story);
        this.notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView comment;
        public TextView comment_date;
        public TextView comment_creator;
        public long story_id;

        public ViewHolder(View v) {
            super(v);
            comment = (TextView) v.findViewById(R.id.comment);
            comment_date = (TextView) v.findViewById(R.id.comment_update_date);
            comment_creator = (TextView) v.findViewById(R.id.comment_creator);
            v.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
         //// TODO: 01/12/15 implement user profile connection
          //  Intent intent = new Intent(mActivity, UserProfileActivity.class);
          //  intent.putExtra("id", user_id);
          //  mActivity.startActivity(intent);

        }
    }


    /*public StoryViewWithCommentAdapter(JSONArray comments, Activity activity) {
        this.comments = comments;
        this.mActivity = activity;
    }
*/

    @Override
    public StoryViewWithCommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_comment, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(getItemCount() == 0){

            Toast.makeText(mActivity.getApplicationContext(),
                    "No comment", Toast.LENGTH_LONG).show();

        }
        try {
            JSONObject obj = comments.get(comments.size()- position-1);
            holder.comment.setText(obj.getString("content"));
            holder.story_id = obj.getLong("story_id");
            user_id = obj.getLong("owner_id");
            long date = obj.getLong("last_edit_date");
            String s = mActivity.getString(R.string.updated_time, Utils.timestampToPrettyString(date),"");

            holder.comment_date.setText(s);

            holder.comment_creator.setText(""+obj.getJSONObject("owner_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}