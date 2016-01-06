package tr.edu.boun.cmpe.sculture.activity;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.widget.ImageView;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;

import tr.edu.boun.cmpe.sculture.BaseApplication;
import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.idlingResource.VolleyIdlingResource;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static tr.edu.boun.cmpe.sculture.CustomMatcher.editTextErrorControl;
import static tr.edu.boun.cmpe.sculture.CustomMatcher.editTextNoError;
import static tr.edu.boun.cmpe.sculture.TestConstants.TEST_BOT_1_ACCESS_TOKEN;
import static tr.edu.boun.cmpe.sculture.TestConstants.TEST_BOT_1_EMAIL;
import static tr.edu.boun.cmpe.sculture.TestConstants.TEST_BOT_1_ID;
import static tr.edu.boun.cmpe.sculture.TestConstants.TEST_BOT_1_STORY_TAG;
import static tr.edu.boun.cmpe.sculture.TestConstants.TEST_BOT_1_USERNAME;

public class StoryCreateActivityTest {
    static IdlingResource idlingResource;
    @Rule
    public ActivityTestRule<StoryCreateActivity> mA = new ActivityTestRule<>(StoryCreateActivity.class);


    @BeforeClass
    public static void classStart() throws NoSuchFieldException {
        BaseApplication.baseApplication.logOut();
        BaseApplication.baseApplication.setUserInfo(TEST_BOT_1_ACCESS_TOKEN, TEST_BOT_1_USERNAME, TEST_BOT_1_EMAIL, TEST_BOT_1_ID);
        Instrumentation instrumentation
                = InstrumentationRegistry.getInstrumentation();
        idlingResource = new VolleyIdlingResource();
        Espresso.registerIdlingResources(idlingResource);
    }


    @AfterClass
    public static void tearDown() throws Exception {
        Espresso.unregisterIdlingResources(idlingResource);
        BaseApplication.baseApplication.logOut();
    }

    @Test
    public void titleTest() {
        onView(allOf(withId(R.id.action_bar), withChild(withText(R.string.title_activity_story_create)))).check(matches(isDisplayed()));
    }

    @Test
    public void errorTest() {
        onView(withId(R.id.action_save)).perform(click());
        onView(withId(R.id.title)).check(matches(editTextErrorControl(R.string.invalid_title)));
        onView(withId(R.id.content)).check(matches(editTextErrorControl(R.string.invalid_content)));

        onView(withId(R.id.title)).perform(replaceText("aaa"));
        onView(withId(R.id.action_save)).perform(click());

        onView(withId(R.id.title)).check(matches(editTextNoError()));
        onView(withId(R.id.content)).check(matches(editTextErrorControl(R.string.invalid_content)));

        onView(withId(R.id.title)).perform(replaceText(""));
        onView(withId(R.id.content)).perform(replaceText("aaa"));
        onView(withId(R.id.action_save)).perform(click());

        onView(withId(R.id.title)).check(matches(editTextErrorControl(R.string.invalid_title)));
        onView(withId(R.id.content)).check(matches(editTextNoError()));
    }

    @Test
    public void createStory() {
        long timestamp = new Date().getTime();
        String title = "title" + timestamp;
        String content = "content " + timestamp;
        String tag = TEST_BOT_1_STORY_TAG;

        onView(withId(R.id.title)).perform(replaceText(title));
        onView(withId(R.id.content)).perform(replaceText(content));
        onView(withId(R.id.searchView)).perform(typeText(tag + ","));
        onView(withId(R.id.action_save)).perform(click());
        onView(withId(R.id.action_search)).check(matches(isDisplayed()));
        onView(isRoot()).perform(swipeLeft());
        onView(isRoot()).perform(swipeLeft());
        onView(isRoot()).perform(swipeLeft());
        onView(withId(R.id.profile_username)).check(matches(withText(TEST_BOT_1_USERNAME)));
        onView(allOf(withText(title), withId(R.id.story_title))).perform(click());
        onView(withId(R.id.story_title)).check(matches(isDisplayed()));
        onView(withId(R.id.dislikeButton)).check(matches(isDisplayed()));


        onView(allOf(instanceOf(ImageView.class), withParent(withParent(withId(R.id.action_bar))))).perform(click());
        onView(withText(R.string.action_delete)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(isRoot()).perform(swipeLeft());
        onView(isRoot()).perform(swipeLeft());
        onView(isRoot()).perform(swipeLeft());
        onView(withText(title)).check(doesNotExist());


    }

}