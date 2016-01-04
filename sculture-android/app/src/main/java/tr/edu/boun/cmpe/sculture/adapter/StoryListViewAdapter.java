package tr.edu.boun.cmpe.sculture.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import tr.edu.boun.cmpe.sculture.BaseApplication;
import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.Utils;
import tr.edu.boun.cmpe.sculture.activity.StoryShowActivity;
import tr.edu.boun.cmpe.sculture.models.response.StoryResponse;

import static tr.edu.boun.cmpe.sculture.Constants.API_IMAGE_GET;
import static tr.edu.boun.cmpe.sculture.Constants.BUNDLE_STORY_ID;

public class StoryListViewAdapter extends RecyclerView.Adapter<StoryListViewAdapter.ViewHolder> {
    private final Activity mActivity;
    private final ArrayList<StoryResponse> stories = new ArrayList<>();


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

        StoryResponse story = stories.get(position);
        holder.story_title.setText(story.title);
        holder.story_id = story.id;
        String s = mActivity.getString(R.string.updated_time, Utils.timestampToPrettyString(story.update_date), story.last_editor.username);
        holder.story_update_date.setText(s);
        String s2 = mActivity.getString(R.string.written_by_username, story.owner.username);
        holder.story_creator.setText(s2);
        if (story.media.size() > 0)
            holder.imageView.setImageUrl(API_IMAGE_GET + story.media.get(0), BaseApplication.baseApplication.mImageLoader);
        else
            holder.imageView.setImageUrl("", BaseApplication.baseApplication.mImageLoader);
    }


    @Override
    public int getItemCount() {
        return stories.size();
    }

    public void addElement(StoryResponse story) {
        if (stories.contains(story))
            return;
        stories.add(story);
        this.notifyDataSetChanged();
    }

    private void removeElement(StoryResponse story) {
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
        public final NetworkImageView imageView;
        public long story_id;

        public ViewHolder(View v) {
            super(v);
            story_title = (TextView) v.findViewById(R.id.story_title);
            story_update_date = (TextView) v.findViewById(R.id.story_update_date);
            story_creator = (TextView) v.findViewById(R.id.story_creator);
            imageView = (NetworkImageView) v.findViewById(R.id.image);
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