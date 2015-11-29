package tr.edu.boun.cmpe.sculture.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;

import tr.edu.boun.cmpe.sculture.R;

public class StoryImageViewAdapter extends RecyclerView.Adapter<StoryImageViewAdapter.ViewHolder> {
    private Activity mActivity;
    private ArrayList<Uri> uris;
    private RecyclerView recyclerView;
    private StoryImageViewAdapter mAdapter;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ImageButton button;
        public Uri uri;

        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.image);
            button = (ImageButton) v.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uris.remove(uri);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }


    public StoryImageViewAdapter(ArrayList<Uri> uris, Activity activity, RecyclerView view) {
        this.uris = uris;
        this.mActivity = activity;
        this.recyclerView = view;
        this.mAdapter = this;
    }


    @Override
    public StoryImageViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);

        return new ViewHolder(v);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageView.setImageURI(uris.get(position));
        holder.uri = uris.get(position);
    }

    @Override
    public int getItemCount() {
        return uris.size();
    }

    public void addElement(Uri uri) {
        uris.add(uri);
        this.notifyDataSetChanged();
    }

}