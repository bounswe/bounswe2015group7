package tr.edu.boun.cmpe.sculture.activity;


import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;

import tr.edu.boun.cmpe.sculture.BaseApplication;
import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.idlingResource.VolleyIdlingResource;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static tr.edu.boun.cmpe.sculture.Constants.BUNDLE_TAG_TITLE;
import static tr.edu.boun.cmpe.sculture.TestConstants.TEST_BOT_1_ACCESS_TOKEN;
import static tr.edu.boun.cmpe.sculture.TestConstants.TEST_BOT_1_EMAIL;
import static tr.edu.boun.cmpe.sculture.TestConstants.TEST_BOT_1_ID;
import static tr.edu.boun.cmpe.sculture.TestConstants.TEST_BOT_1_USERNAME;
import static tr.edu.boun.cmpe.sculture.TestConstants.TEST_BOT_2_STORY_1_TAG;
import static tr.edu.boun.cmpe.sculture.TestConstants.TEST_BOT_2_STORY_1_TITLE;

public class TagActivityTest {
    static IdlingResource idlingResource;
    @Rule
    public ActivityTestRule<TagActivity> mA = new ActivityTestRule<>(TagActivity.class, false, false);


    @BeforeClass
    public static void classStart() throws NoSuchFieldException {
        BaseApplication.baseApplication.logOut();
        BaseApplication.baseApplication.setUserInfo(TEST_BOT_1_ACCESS_TOKEN, TEST_BOT_1_USERNAME, TEST_BOT_1_EMAIL, TEST_BOT_1_ID);
        Instrumentation instrumentation
                = InstrumentationRegistry.getInstrumentation();
        idlingResource = new VolleyIdlingResource();
        Espresso.registerIdlingResources(idlingResource);
    }

    @Before
    public void start() {
        Intent i = new Intent();
        i.putExtra(BUNDLE_TAG_TITLE, TEST_BOT_2_STORY_1_TAG);
        mA.launchActivity(i);
    }


    @AfterClass
    public static void tearDown() throws Exception {
        Espresso.unregisterIdlingResources(idlingResource);
        BaseApplication.baseApplication.logOut();
    }

    @Test
    public void titleTest() {
        onView(allOf(withId(R.id.action_bar), withChild(withText(TEST_BOT_2_STORY_1_TAG)))).check(matches(isDisplayed()));
    }

    @Test
    public void listTest() {
        onView(withId(R.id.story_title)).check(matches(withText(TEST_BOT_2_STORY_1_TITLE)));
        onView(withId(R.id.story_title)).perform(click());
        onView(withId(R.id.likeButton)).check(matches(isDisplayed()));
    }

    @Test
    public void editDescriptionTest() {
        String text = new Date().getTime() + " test description";
        onView(withId(R.id.action_edit)).perform(click());
        onView(withId(R.id.tagDescription)).perform(replaceText(text));
        onView(withId(R.id.action_save)).perform(click());
        onView(withText(text)).check(matches(isDisplayed()));
        onView(withId(R.id.action_edit)).perform(click());
        pressBack();
        onView(withText(text)).check(matches(isDisplayed()));
        onView(withId(R.id.action_edit)).perform(click());
        onView(withId(R.id.action_save)).perform(click());
        onView(withText(text)).check(matches(isDisplayed()));
    }
}