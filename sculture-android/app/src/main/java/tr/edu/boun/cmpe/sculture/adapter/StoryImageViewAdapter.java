package tr.edu.boun.cmpe.sculture.adapter;

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
    private final ArrayList<Uri> uris;

    public StoryImageViewAdapter(ArrayList<Uri> uris) {
        this.uris = uris;
    }

    @Override
    public StoryImageViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);

        return new ViewHolder(v);
    }

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

    private void removeElement(Uri uri) {
        uris.remove(uri);
        removeElement(uris.indexOf(uri));
    }

    private void removeElement(int index) {
        uris.remove(index);
        this.notifyItemRemoved(index);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageView;
        public final ImageButton button;
        public Uri uri;

        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.image);
            button = (ImageButton) v.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeElement(uri);
                }
            });
        }
    }
}