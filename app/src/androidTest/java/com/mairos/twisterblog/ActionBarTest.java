package com.mairos.twisterblog;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Mike on 10.01.2015.
 */
@LargeTest
public class ActionBarTest extends ActivityInstrumentationTestCase2<MainActivity_> {
    @SuppressWarnings("deprecation")
    public ActionBarTest() {
        // This constructor was deprecated - but we want to support lower API levels.
        super("com.mairos.twisterblog", MainActivity_.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        // Espresso will not launch our activity for us, we must launch it via getActivity().
        getActivity();
    }

    public void testActivityTitle() {
        assertEquals("Twister blog", getActivity().getTitle());
    }

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
}
