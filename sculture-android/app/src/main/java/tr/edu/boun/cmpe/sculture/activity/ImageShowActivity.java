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
import tr.edu.boun.cmpe.sculture.BuildConfig;
import tr.edu.boun.cmpe.sculture.R;

import static tr.edu.boun.cmpe.sculture.Constants.BUNDLE_INDEX;
import static tr.edu.boun.cmpe.sculture.Constants.BUNDLE_MEDIA_IDS;

public class ImageShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);
        Bundle bundle = getIntent().getExtras();
        ArrayList<String> image_ids = null;
        int index = 0;
        if (bundle != null) {
            image_ids = bundle.getStringArrayList(BUNDLE_MEDIA_IDS);
            index = bundle.getInt(BUNDLE_INDEX);
        }


        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        ImagePagerAdapter adapter = new ImagePagerAdapter();
        adapter.ids = image_ids;
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(index);

    }

    private class ImagePagerAdapter extends PagerAdapter {


        private ArrayList<String> ids;

        @Override
        public int getCount() {
            return ids.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Context context = ImageShowActivity.this;
            NetworkImageView imageView = new NetworkImageView(context);

            int padding = context.getResources().getDimensionPixelSize(
                    R.dimen.padding_medium);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setImageUrl(BuildConfig.API_BASE_URL + "/image/get/" + ids.get(position), BaseApplication.baseApplication.mImageLoader);

            container.addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView) object);
        }
    }
}
