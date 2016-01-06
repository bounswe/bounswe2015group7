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
import java.util.List;

import tr.edu.boun.cmpe.sculture.BaseApplication;
import tr.edu.boun.cmpe.sculture.ImageLocation;
import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.activity.ImageShowActivity;
import tr.edu.boun.cmpe.sculture.LargeBundle;

import static tr.edu.boun.cmpe.sculture.Constants.API_IMAGE_GET;
import static tr.edu.boun.cmpe.sculture.Constants.BUNDLE_INDEX;
import static tr.edu.boun.cmpe.sculture.Constants.BUNDLE_MEDIA_IDS;

/**
 * Recycler view adapter which stores thumbnails of media images
 */
public class StoryImageViewAdapter extends RecyclerView.Adapter<StoryImageViewAdapter.ViewHolder> {
    private ArrayList<ImageLocation> imageLocations = new ArrayList<>();

    private boolean isEditable = false;

    public StoryImageViewAdapter(boolean isEditable) {
        this.isEditable = isEditable;
    }

    /**
     * Adds a local image to the adapter
     *
     * @param uri Uri of local file
     */
    public void add(Uri uri) {
        imageLocations.add(new ImageLocation(uri));
        notifyItemInserted(imageLocations.size() - 1);
    }

    /**
     * Adds a remote image to the adapter
     *
     * @param id The ID of the image
     */
    public void add(String id) {
        imageLocations.add(new ImageLocation(id));
        notifyItemInserted(imageLocations.size() - 1);
    }

    /**
     * Adds a list of local images to the adapter
     *
     * @param uris List of the uris of local images
     */
    public void addUris(List<Uri> uris) {
        for (Uri uri : uris)
            add(uri);
    }

    /**
     * Adds a list of remote images to the adapter
     *
     * @param ids List of the IDs of remote images
     */
    public void addIds(List<String> ids) {
        for (String id : ids)
            imageLocations.add(new ImageLocation(id));
    }

    /**
     * Removes an image from adapter
     *
     * @param imageLocation Image
     */
    public void removeElement(ImageLocation imageLocation) {
        int i = imageLocations.indexOf(imageLocation);
        imageLocations.remove(imageLocation);
        notifyItemRemoved(i);
    }

    /**
     * Removes all images from the adapter
     */
    public void removeAll() {
        imageLocations.clear();
        notifyDataSetChanged();
    }

    @Override
    public StoryImageViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_image_edit, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageLocation imageLocation = imageLocations.get(position);
        if (imageLocation.isLocal()) {
            holder.local_imageView.setVisibility(View.VISIBLE);
            holder.local_imageView.setImageURI(imageLocation.getUri());
            holder.imageView.setVisibility(View.GONE);
        } else {
            holder.imageView.setVisibility(View.VISIBLE);
            holder.imageView.setImageUrl(API_IMAGE_GET + imageLocation.getId(), BaseApplication.baseApplication.mImageLoader);
            holder.local_imageView.setVisibility(View.GONE);
        }
        holder.imageLocation = imageLocation;
    }

    @Override
    public int getItemCount() {
        return imageLocations.size();
    }

    public ArrayList<ImageLocation> getImageLocations() {
        return imageLocations;
    }

    /**
     * View holder of a image with delete button
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final NetworkImageView imageView;
        public final ImageView local_imageView;
        public final ImageButton button;
        private ImageLocation imageLocation;

        public ViewHolder(View v) {
            super(v);
            imageView = (NetworkImageView) v.findViewById(R.id.image);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ImageShowActivity.class);

                    intent.putExtra(BUNDLE_MEDIA_IDS, LargeBundle.addItem(imageLocations));
                    intent.putExtra(BUNDLE_INDEX, imageLocations.indexOf(imageLocation));
                    v.getContext().startActivity(intent);
                }
            });
            local_imageView = (ImageView) v.findViewById(R.id.image_local);
            local_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ImageShowActivity.class);
                    intent.putExtra(BUNDLE_MEDIA_IDS, LargeBundle.addItem(imageLocations));
                    intent.putExtra(BUNDLE_INDEX, imageLocations.indexOf(imageLocation));
                    v.getContext().startActivity(intent);
                }
            });
            button = (ImageButton) v.findViewById(R.id.button);
            if (isEditable)
                button.setVisibility(View.VISIBLE);
            else
                button.setVisibility(View.GONE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeElement(imageLocation);
                }
            });
        }
    }
}