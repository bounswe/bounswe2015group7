package tr.edu.boun.cmpe.sculture.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.Utils;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Activity mActivity;
    private JSONArray stories;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView story_title;
        public TextView story_update_date;
        public TextView story_creator;


        public ViewHolder(View v) {
            super(v);
            story_title = (TextView) v.findViewById(R.id.story_title);
            story_update_date = (TextView) v.findViewById(R.id.story_update_date);
            story_creator = (TextView) v.findViewById(R.id.story_creator);
            v.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), story_title.getText().toString(), Toast.LENGTH_LONG).show();
        }
    }


    public RecyclerViewAdapter(JSONArray stories, Activity activity) {
        this.stories = stories;
        this.mActivity = activity;
    }


    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.searched_story, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        try {
            JSONObject obj = stories.getJSONObject(position);
            holder.story_title.setText(obj.getString("title"));


            long date = obj.getLong("last_edit_date");
            String s = mActivity.getString(R.string.updated_time, Utils.timespamptToPrettyStrig(date), obj.get("last_editor_id"));

            holder.story_update_date.setText(s);

            String s2 = mActivity.getString(R.string.written_by_username, obj.getString("owner_id"));

            holder.story_creator.setText(s2);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return stories.length();
    }
}