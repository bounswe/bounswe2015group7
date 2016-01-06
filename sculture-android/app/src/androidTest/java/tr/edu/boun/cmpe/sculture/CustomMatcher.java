package tr.edu.boun.cmpe.sculture;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class CustomMatcher {


    /**
     * Matches a edit text with given error text.
     *
     * @param id Resource id of error string
     */
    public static Matcher<View> editTextErrorControl(final int id) {
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {

                String s = view.getResources().getString(id);
                EditText editText = (EditText) view;
                return editText.getError().toString().equals(s);

            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is on the error of text view");
            }
        };
    }

    /**
     * Matcher a edit text which has no error.
     */
    public static Matcher<View> editTextNoError() {
        return new org.hamcrest.TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                EditText editText = (EditText) view;
                return editText.getError() == null;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("No error");
            }
        };
    }


    protected static Matcher<View> firstElementOfRcycler(final int id) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                if (item.getParent().getClass() == RecyclerView.class) {
                    RecyclerView parent = (RecyclerView) item.getParent();
                    if (parent.getId() == id) {
                        if (parent.getChildAt(0).getId() == item.getId())
                            return true;
                    }
                }
                return false;

            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }

}