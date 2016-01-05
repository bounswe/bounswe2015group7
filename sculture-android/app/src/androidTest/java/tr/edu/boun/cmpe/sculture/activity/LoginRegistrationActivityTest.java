package tr.edu.boun.cmpe.sculture.activity;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;

import tr.edu.boun.cmpe.sculture.BaseApplication;
import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.idlingResource.VolleyIdlingResource;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static tr.edu.boun.cmpe.sculture.CustomMatcher.editTextErrorControl;
import static tr.edu.boun.cmpe.sculture.TestConstants.*;

public class LoginRegistrationActivityTest {
    static IdlingResource idlingResource;
    @Rule
    public ActivityTestRule<LoginRegistrationActivity> mA = new ActivityTestRule<>(LoginRegistrationActivity.class);


    @BeforeClass
    public static void classStart() {
        BaseApplication.baseApplication.logOut();
    }

    @Before
    public void setUp() throws Exception {
        Instrumentation instrumentation
                = InstrumentationRegistry.getInstrumentation();
        idlingResource = new VolleyIdlingResource();
        Espresso.registerIdlingResources(idlingResource);
    }

    @After
    public void tearDown() throws Exception {
        Espresso.unregisterIdlingResources(idlingResource);
        BaseApplication.baseApplication.logOut();
    }

    @Test
    public void invalidPasswordTest() {
        onView(withId(R.id.email)).perform(typeText(TEST_BOT_1_EMAIL));
        onView(withId(R.id.password)).perform(typeText("as"));
        onView(withId(R.id.loginRegistrationButton)).perform(click());
        onView(withId(R.id.password)).check(matches(editTextErrorControl(R.string.error_invalid_password)));
    }

    @Test
    public void invalidEmailTest() {
        onView(withId(R.id.email)).perform(typeText("aaa"));
        onView(withId(R.id.password)).perform(typeText(TEST_BOT_1_PASSWORD));
        onView(withId(R.id.loginRegistrationButton)).perform(click());
        onView(withId(R.id.email)).check(matches(editTextErrorControl(R.string.error_invalid_email)));
    }

    @Test
    public void userNotExistTest() {
        onView(withId(R.id.email)).perform(typeText(new Date().getTime() + "_test@sculture.com"));
        onView(withId(R.id.password)).perform(typeText(TEST_BOT_1_PASSWORD));
        onView(withId(R.id.loginRegistrationButton)).perform(click());
        onView(withId(R.id.email)).check(matches(editTextErrorControl(R.string.user_not_exist)));
    }

    @Test
    public void wrongPasswordTest() {
        onView(withId(R.id.email)).perform(typeText(TEST_BOT_1_EMAIL));
        onView(withId(R.id.password)).perform(typeText("asdfasdfasdf"));
        onView(withId(R.id.loginRegistrationButton)).perform(click());
        onView(withId(R.id.password)).check(matches(editTextErrorControl(R.string.wrong_password)));
    }

    @Test
    public void existedUserTest() {
        onView(withId(R.id.loginRegistrationSwitchButton)).perform(click());
        onView(withId(R.id.email)).perform(typeText(TEST_BOT_1_EMAIL));
        onView(withId(R.id.password)).perform(typeText("asdfasdfasdf"));
        onView(withId(R.id.confirmPassword)).perform(typeText("asdfasdfasdf"));
        onView(withId(R.id.username)).perform(typeText("tester"));
    }

    @Test
    public void loginTest() {
        onView(withId(R.id.email)).perform(typeText(TEST_BOT_1_EMAIL));
        onView(withId(R.id.password)).perform(typeText(TEST_BOT_1_PASSWORD));
        onView(withId(R.id.loginRegistrationButton)).perform(click());
        onView(withId(R.id.action_search)).check(matches(isDisplayed()));
    }
}