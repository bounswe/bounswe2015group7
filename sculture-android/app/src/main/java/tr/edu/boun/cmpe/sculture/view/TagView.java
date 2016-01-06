package tr.edu.boun.cmpe.sculture.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokenautocomplete.TokenCompleteTextView;

import tr.edu.boun.cmpe.sculture.R;

/**
 * A tokenized text view for shoing tags
 */
public class TagView extends TokenCompleteTextView<String> {
    public TagView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View getViewForObject(String person) {
        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout) l.inflate(R.layout.tag_token, (ViewGroup) TagView.this.getParent(), false);
        ((TextView) view.findViewById(R.id.tag)).setText(person);
        return view;
    }

    @Override
    protected String defaultObject(String completionText) {
        return completionText.replace(' ', '-');
    }
}