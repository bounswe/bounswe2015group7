package tr.edu.boun.cmpe.sculture.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import tr.edu.boun.cmpe.sculture.BaseApplication;
import tr.edu.boun.cmpe.sculture.ImageLocation;
import tr.edu.boun.cmpe.sculture.LargeBundle;
import tr.edu.boun.cmpe.sculture.R;

import static tr.edu.boun.cmpe.sculture.Constants.API_IMAGE_GET;
import static tr.edu.boun.cmpe.sculture.Constants.BUNDLE_INDEX;
import static tr.edu.boun.cmpe.sculture.Constants.BUNDLE_MEDIA_IDS;

public class ImageShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);
        Bundle bundle = getIntent().getExtras();
        ArrayList<ImageLocation> imageLocations = null;
        int index = 0;
        if (bundle != null) {
            int address = bundle.getInt(BUNDLE_MEDIA_IDS, -1);
            if (address != -1)
                imageLocations = (ArrayList<ImageLocation>) LargeBundle.getItem(address);
            index = bundle.getInt(BUNDLE_INDEX);
        }


        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        ImagePagerAdapter adapter = new ImagePagerAdapter();
        adapter.imageLocations = imageLocations;
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(index);

    }

    private class ImagePagerAdapter extends PagerAdapter {


        private ArrayList<ImageLocation> imageLocations;

        @Override
        public int getCount() {
            return imageLocations.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Context context = ImageShowActivity.this;

            int padding = context.getResources().getDimensionPixelSize(
                    R.dimen.padding_medium);
            ImageLocation imageLocation = imageLocations.get(position);
            if (imageLocation.isLocal()) {
                ImageView imageView = new ImageView(context);
                imageView.setPadding(padding, padding, padding, padding);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setImageURI(imageLocation.getUri());
                container.addView(imageView, 0);
                return imageView;
            } else {

                NetworkImageView imageView = new NetworkImageView(context);


                imageView.setPadding(padding, padding, padding, padding);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setImageUrl(API_IMAGE_GET + imageLocation.getId(), BaseApplication.baseApplication.mImageLoader);

                container.addView(imageView, 0);
                return imageView;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView) object);
        }
    }
}
