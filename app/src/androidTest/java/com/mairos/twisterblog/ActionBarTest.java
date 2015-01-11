package com.mairos.twisterblog;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Mike on 10.01.2015.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ActionBarTest extends ActivityInstrumentationTestCase2<MainActivity_> {

    private MainActivity_ mActivity;

    @SuppressWarnings("deprecation")
    public ActionBarTest() {
        // This constructor was deprecated - but we want to support lower API levels.
        super(MainActivity_.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();

        // As the way to access Instrumentation is changed in the new runner, we need to inject it
        // manually into ActivityInstrumentationTestCase2.
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());

        // Espresso will not launch our activity for us, we must launch it via getActivity().
        mActivity = getActivity();
    }

    @Test
    public void testActivityTitle() {
        assertEquals("Twister blog", mActivity.getTitle());
    }

    @Test
    public void testAddPostMenuItem() {
        onView(withId(R.id.action_add))
                .perform(click());

        onView(withId(R.id.button_add))
                .check(matches(withText("add post")));

        onView(withId(R.id.text_title))
                .perform(typeText("Post from Espresso."));

        onView(withId(R.id.text_body))
                .perform(typeText("This is the post created from Espresso testing library."));

        onView(withId(R.id.button_add))
                .perform(click());
    }

    @After
    public void tearDown() throws Exception {
        // Make sure that we call the tearDown() method of ActivityInstrumentationTestCase2
        // to clean up and not leak any objects.
        super.tearDown();
    }
}
