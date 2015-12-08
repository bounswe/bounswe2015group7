package tr.edu.boun.cmpe.sculture.adapter;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tr.edu.boun.cmpe.sculture.Constants;
import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.Utils;
import tr.edu.boun.cmpe.sculture.models.response.CommentResponse;
import tr.edu.boun.cmpe.sculture.models.response.FullStoryResponse;

import static tr.edu.boun.cmpe.sculture.BaseApplication.baseApplication;

public class StoryViewWithCommentAdapter extends RecyclerView.Adapter<ViewHolder> {
    private static final int VIEW_TYPE_STORY = 1;
    private static final int VIEW_TYPE_COMMENT_EDIT = 2;
    private static final int VIEW_TYPE_COMMENT = 3;
    private boolean is_story_added = false;
    private FullStoryResponse story;
    private final ArrayList<CommentResponse> comments = new ArrayList<>();

    private Activity mActivity;

    class StoryViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView content;
        private TextView writer;
        private TextView tags;
        private TextView update;
        private RecyclerView recyclerView;
        private StoryImageViewAdapter adapter;
        private long owner_id;
        private long editor_id;
        private List<String> media_ids = new ArrayList<>();

        public StoryViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.story_title);
            content = (TextView) itemView.findViewById(R.id.storyContent);
            writer = (TextView) itemView.findViewById(R.id.story_creation);
            tags = (TextView) itemView.findViewById(R.id.storyTags);
            update = (TextView) itemView.findViewById(R.id.story_update);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.story_media_recycler);

            LinearLayoutManager llm = new LinearLayoutManager(itemView.getContext());
            llm.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(llm);
            adapter = new StoryImageViewAdapter(null, media_ids, false);
            recyclerView.setAdapter(adapter);
            writer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("CLICK", "Writer clicked");
                    //TODO OPEN USER PAGE
                }
            });
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("CLICK", "Update clicked");
                    //TODO OPEN USER PAGE
                }
            });
        }
    }

    class CommentEditViewHolder extends RecyclerView.ViewHolder {

        private EditText comment;
        private Button submit;

        public CommentEditViewHolder(View itemView) {
            super(itemView);
            comment = (EditText) itemView.findViewById(R.id.comment);
            submit = (Button) itemView.findViewById(R.id.comment_submit);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (comment.getText().toString().equals(""))
                        return;
                    JSONObject request = new JSONObject();
                    try {
                        request.put("storyId", story.id);
                        request.put("content", comment.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    comment.setText("");
                    Utils.addRequest(Constants.API_COMMENT_NEW, request, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            CommentResponse commentResponse = new CommentResponse(response);
                            addComment(commentResponse);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                VolleyError e = new VolleyError(new String(error.networkResponse.data));
                                Log.i("ERROR", e.toString());
                            } else Log.i("ERROR", error.toString());
                            //TODO ERROR HANDLING
                        }
                    }, null);

                    Log.i("CLICK", "Comment submit clicked");
                    //TODO CONNECT API
                }
            });
        }
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        private EditText comment;
        private TextView writer;
        private TextView time;
        private TextView editSave;
        private TextView save;
        private long comment_id;
        private long owner_id;

        private boolean is_on_edit = false;
        private String previous = "";

        public CommentViewHolder(View itemView) {
            super(itemView);
            comment = (EditText) itemView.findViewById(R.id.comment);
            writer = (TextView) itemView.findViewById(R.id.comment_creator);
            time = (TextView) itemView.findViewById(R.id.comment_update_date);
            editSave = (TextView) itemView.findViewById(R.id.edit_cancel_text);
            save = (TextView) itemView.findViewById(R.id.save_Text);

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("CLICK", "Save clicked");
                    //TODO save
                }
            });
            writer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("CLICK", "Writer clicked");
                    //TODO OPEN USER PAGE
                }
            });
            editSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (is_on_edit) {
                        comment.setEnabled(false);
                        editSave.setText(mActivity.getString(R.string.edit));
                        comment.setText(previous);
                        save.setVisibility(View.GONE);
                    } else {
                        previous = comment.getText().toString();
                        comment.setEnabled(true);
                        editSave.setText(mActivity.getString(R.string.cancel));
                        save.setVisibility(View.VISIBLE);
                    }
                    is_on_edit = !is_on_edit;
                }
            });
        }
    }

    public int getItemViewType(int position) {
        if (position == 0)
            return VIEW_TYPE_STORY;
        else if (position == 1)
            return VIEW_TYPE_COMMENT_EDIT;
        else
            return VIEW_TYPE_COMMENT;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case VIEW_TYPE_STORY:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_story_view, parent, false);
                return new StoryViewHolder(v);

            case VIEW_TYPE_COMMENT_EDIT:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_comment_edit, parent, false);
                return new CommentEditViewHolder(v);

            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_comment, parent, false);
                return new CommentViewHolder(v);

        }

    }


    public StoryViewWithCommentAdapter(Activity activity) {

        this.mActivity = activity;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (position) {
            case 0:
                StoryViewHolder viewHolder = (StoryViewHolder) holder;

                viewHolder.title.setText(story.title);
                viewHolder.content.setText(story.content);
                viewHolder.writer.setText(story.owner.username);
                viewHolder.owner_id = story.owner.id;
                viewHolder.update.setText(mActivity.getString(R.string.updated_time, Utils.timestampToPrettyString(story.update_date), story.last_editor.username));
                viewHolder.editor_id = story.last_editor.id;
                viewHolder.media_ids.clear();
                viewHolder.media_ids.addAll(story.media);

                String tags = "";

                //TODO Clickable tags
                for (int i = 0; i < story.tags.size(); i++) {
                    tags += story.tags.get(i) + ", ";
                }
                viewHolder.tags.setText(tags);
                viewHolder.adapter.notifyDataSetChanged();
                break;
            case 1:
                break;
            default:
                CommentViewHolder commentViewHolder = (CommentViewHolder) holder;
                CommentResponse commentResponse = comments.get(position - 2);

                commentViewHolder.comment_id = commentResponse.comment_id;
                commentViewHolder.owner_id = commentResponse.owner_id;
                commentViewHolder.writer.setText(commentResponse.owner_username);
                commentViewHolder.comment.setText(commentResponse.content);
                commentViewHolder.time.setText(Utils.timestampToPrettyString(commentResponse.last_edit_date));

                commentViewHolder.comment.setEnabled(false);
                if (commentResponse.owner_id == baseApplication.getUSER_ID()) {
                    commentViewHolder.editSave.setVisibility(View.VISIBLE);
                } else {
                    commentViewHolder.editSave.setVisibility(View.GONE);
                }
                break;
        }
    }


    @Override
    public int getItemCount() {
        int size = comments.size();
        if (is_story_added)
            size += 2;
        return size;
    }

    public void addStory(FullStoryResponse story) {
        this.story = story;
        is_story_added = true;
        this.notifyItemChanged(0);
        this.notifyItemChanged(1);

    }

    public void addComment(CommentResponse commentResponse) {
        comments.add(commentResponse);
        this.notifyItemInserted(comments.size() + 2);
    }
}