package tr.edu.boun.cmpe.sculture.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import tr.edu.boun.cmpe.sculture.BaseApplication;
import tr.edu.boun.cmpe.sculture.BuildConfig;
import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.activity.ImageShowActivity;

import static tr.edu.boun.cmpe.sculture.Constants.BUNDLE_INDEX;
import static tr.edu.boun.cmpe.sculture.Constants.BUNDLE_MEDIA_IDS;

public class StoryImageViewAdapter extends RecyclerView.Adapter<StoryImageViewAdapter.ViewHolder> {
    private ArrayList<Uri> uris = null;
    private boolean is_create;
    private ArrayList<String> urls = null;

    public StoryImageViewAdapter(ArrayList<Uri> uris, ArrayList<String> urls, boolean is_create) {
        //TODO This class could be better
        this.uris = uris;
        this.urls = urls;
        this.is_create = is_create;
    }


    @Override
    public StoryImageViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_image_edit, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (is_create) {
            holder.local_imageView.setVisibility(View.VISIBLE);
            holder.local_imageView.setImageURI(uris.get(position));
            holder.imageView.setVisibility(View.GONE);

        } else {
            holder.imageView.setVisibility(View.VISIBLE);
            holder.imageView.setImageUrl(BuildConfig.API_BASE_URL + "/image/get/" + urls.get(position), BaseApplication.baseApplication.mImageLoader);
            holder.local_imageView.setVisibility(View.GONE);
        }
        holder.index = position;

    }

    @Override
    public int getItemCount() {
        if (urls != null)
            return urls.size();
        else if (uris != null)
            return uris.size();
        return 0;
    }

    public void addElement(Uri uri) {
        if (!uris.contains(uri)) {
            uris.add(uri);
            this.notifyItemInserted(uris.size() - 1);
        }
    }

    private void removeElement(Uri uri) {
        removeElement(uris.indexOf(uri));
    }

    private void removeElement(int index) {
        uris.remove(index);
        this.notifyItemRemoved(index);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final NetworkImageView imageView;
        public final ImageView local_imageView;
        public final ImageButton button;
        private int index = -1;

        public ViewHolder(View v) {
            super(v);
            imageView = (NetworkImageView) v.findViewById(R.id.image);
            if (!is_create) {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ImageShowActivity.class);
                        intent.putExtra(BUNDLE_MEDIA_IDS, urls);
                        intent.putExtra(BUNDLE_INDEX, index);
                        v.getContext().startActivity(intent);

                        //TODO Add image view activity here
                    }
                });
            }
            local_imageView = (ImageView) v.findViewById(R.id.image_local);

            button = (ImageButton) v.findViewById(R.id.button);
            if (!is_create) {
                button.setVisibility(View.GONE);
            }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeElement(index);
                }
            });
        }
    }
}