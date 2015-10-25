package tr.edu.boun.cmpe.sculture;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class UITests {

    @Rule
    public ActivityTestRule<MainActivity> mA = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void MainMenuTest() {
        onView(withId(R.id.fab)).perform(click());
        onView(withText("Replace with your own action")).check(matches(isDisplayed()));
    }

}
