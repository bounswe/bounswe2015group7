package tr.edu.boun.cmpe.sculture.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.Utils;
import tr.edu.boun.cmpe.sculture.activity.StoryShowActivity;

import static tr.edu.boun.cmpe.sculture.Constants.BUNDLE_STORY_ID;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_ID;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_LAST_EDITOR;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_OWNER;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_TITLE;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_UPDATE_DATE;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_USERNAME;

public class StoryListViewAdapter extends RecyclerView.Adapter<StoryListViewAdapter.ViewHolder> {
    private final Activity mActivity;
    private final ArrayList<JSONObject> stories = new ArrayList<>();


    public StoryListViewAdapter(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public StoryListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_searched_story, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            JSONObject obj = stories.get(position);
            holder.story_title.setText(obj.getString(FIELD_TITLE));
            holder.story_id = obj.getLong(FIELD_ID);

            long date = obj.getLong(FIELD_UPDATE_DATE);
            String s = mActivity.getString(R.string.updated_time, Utils.timestampToPrettyString(date), obj.getJSONObject(FIELD_LAST_EDITOR).getString(FIELD_USERNAME));

            holder.story_update_date.setText(s);

            String s2 = mActivity.getString(R.string.written_by_username, obj.getJSONObject(FIELD_OWNER).getString(FIELD_USERNAME));

            holder.story_creator.setText(s2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    public void addElement(JSONObject story) {
        stories.add(story);
        this.notifyDataSetChanged();
    }

    private void removeElement(JSONObject story) {
        stories.remove(story);
        removeElement(stories.indexOf(story));
    }

    private void removeElement(int index) {
        stories.remove(index);
        this.notifyItemRemoved(index);
    }

    public void clearElements() {
        stories.clear();
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView story_title;
        public final TextView story_update_date;
        public final TextView story_creator;
        public long story_id;

        public ViewHolder(View v) {
            super(v);
            story_title = (TextView) v.findViewById(R.id.story_title);
            story_update_date = (TextView) v.findViewById(R.id.story_update_date);
            story_creator = (TextView) v.findViewById(R.id.story_creator);
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mActivity, StoryShowActivity.class);
            intent.putExtra(BUNDLE_STORY_ID, story_id);
            mActivity.startActivity(intent);

        }
    }
}