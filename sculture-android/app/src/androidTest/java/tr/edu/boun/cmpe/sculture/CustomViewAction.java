package tr.edu.boun.cmpe.sculture;

import android.graphics.Rect;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

public class CustomViewAction {

    /**
     * Clicks the text inside of given text view.
     *
     * @param id Resource id of the text which will be clicked
     * @return Click action
     */
    public static ViewAction clickableSpanClick(final int id) {
        return new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {
                        String s = getTargetContext().getString(id);

                        TextView tv = (TextView) view;
                        Layout l = tv.getLayout();
                        SpannableString ss = (SpannableString) tv.getText();
                        String allText = ss.toString();
                        int startIndex = allText.indexOf(s);
                        ClickableSpan[] cs = ss.getSpans(startIndex, startIndex + s.length(), ClickableSpan.class);
                        assertNotEquals(cs.length, 0);
                        assertFalse(cs.length > 1);
                        ClickableSpan c = cs[0];
                        Rect parentTextViewRect = new Rect();

                        double startOffsetOfClickedText = ss.getSpanStart(c);
                        double endOffsetOfClickedText = ss.getSpanEnd(c);
                        double startXCoordinatesOfClickedText = l.getPrimaryHorizontal((int) startOffsetOfClickedText);
                        double endXCoordinatesOfClickedText = l.getPrimaryHorizontal((int) endOffsetOfClickedText);

                        int[] parentTextViewLocation = {0, 0};


                        double parentTextViewTopAndBottomOffset = (
                                parentTextViewLocation[1] -
                                        tv.getScrollY() +
                                        tv.getCompoundPaddingTop()
                        );
                        parentTextViewRect.top += parentTextViewTopAndBottomOffset;
                        parentTextViewRect.bottom += parentTextViewTopAndBottomOffset;

                        parentTextViewRect.left += (
                                parentTextViewLocation[0] +
                                        startXCoordinatesOfClickedText +
                                        tv.getCompoundPaddingLeft() -
                                        tv.getScrollX()
                        );
                        parentTextViewRect.right = (int) (
                                parentTextViewRect.left +
                                        endXCoordinatesOfClickedText -
                                        startXCoordinatesOfClickedText
                        );

                        int x = parentTextViewRect.centerX();
                        int y = parentTextViewRect.centerY();


                        final int[] screenPos = new int[2];
                        view.getLocationOnScreen(screenPos);

                        final float screenX = screenPos[0] + x;
                        final float screenY = screenPos[1] + y;
                        return new float[]{screenX, screenY};

                    }
                },
                Press.FINGER);
    }


    public static ViewAction clickChildViewWithId(final int id) {



        return new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {
                        View myView = view.findViewById(id);


                        final int[] screenPos = new int[2];
                        myView.getLocationOnScreen(screenPos);

                        int w = myView.getWidth();
                        int h = myView.getHeight();

                        final float screenX = screenPos[0] + w/2;
                        final float screenY = screenPos[1] + h/2;


                        Log.i ("LOCATIONFD", screenX  + "+++" +screenY);
                        return new float[]{screenX, screenY};
                    }
                },
                Press.FINGER);
    }


}
