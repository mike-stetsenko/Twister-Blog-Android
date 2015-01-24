package com.mairos.twisterblog;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import com.android.support.test.deps.guava.base.Preconditions;
import com.mairos.twisterblog.model.Post;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.timroes.android.listview.EnhancedListView;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


/**
 * Created by Mike on 10.01.2015.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class PostsPageTest extends ActivityInstrumentationTestCase2<MainActivity_> {

    private MainActivity_ mActivity;

    private static final Post testPost = new Post(12345, "Post from Espresso",
            "This is the post created from Espresso testing library", "created_at", "updated_at");


    @SuppressWarnings("deprecation")
    public PostsPageTest() {
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
    @Order(order=1)
    public void testActivityTitle() {
        assertEquals("Twister blog", mActivity.getTitle());
    }

    @Test
    @Order(order=2)
    public void testAddPostMenuItem() {
        onView(withId(R.id.action_add))
                .perform(click());

        onView(withId(R.id.button_add))
                .check(matches(withText("add post")));

        onView(withId(R.id.text_title))
                .perform(typeText(testPost.title));

        onView(withId(R.id.text_body))
                .perform(typeText(testPost.body));

        onView(withId(R.id.button_add))
                .perform(click());
    }

    @Order(order=3)
    @Test
    public void testPostListClick(){
        EnhancedListView listPosts = (EnhancedListView) getActivity().findViewById(R.id.list_posts);
        Preconditions.checkNotNull(listPosts, "listPosts is null");
        Post post = (Post) listPosts.getItemAtPosition(listPosts.getCount()-1);
        assertEquals(testPost.title, post.title);
        onView(Matchers.allOf(ViewMatchers.withId(R.id.text_title),
                ViewMatchers.hasSibling(ViewMatchers.withText(post.title)))).perform(click());
        onView(withId(R.id.post_content))
                .check(matches(withText(post.body)));
        onView(isRoot()).perform(pressBack());
    }

    @Order(order=4)
    @Test
    public void testDeleteItemFromList(){

        onView(Matchers.allOf(ViewMatchers.withId(R.id.text_title),
                ViewMatchers.hasSibling(ViewMatchers.withText(testPost.title)))).perform(swipeRight());

        assertNotNull(ViewMatchers.withText(testPost.title));
    }

    @After
    public void tearDown() throws Exception {
        // Make sure that we call the tearDown() method of ActivityInstrumentationTestCase2
        // to clean up and not leak any objects.
        super.tearDown();
    }
}
