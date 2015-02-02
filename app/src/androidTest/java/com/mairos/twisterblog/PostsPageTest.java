package com.mairos.twisterblog;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import com.mairos.twisterblog.model.Post;
import com.mairos.twisterblog.network.RequestStatusObject;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.text.SimpleDateFormat;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.android.support.test.deps.guava.base.Preconditions.checkNotNull;

/**
 * Created by Mike on 10.01.2015.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class PostsPageTest extends ActivityInstrumentationTestCase2<MainActivity_> {

    private MainActivity_ mActivity;

    private RequestStatusIdlingResource mRequestIR;

    private static final SimpleDateFormat form = new SimpleDateFormat("SSS");
    private static final Post sTestPost = new Post(12345, "post from espresso "/* + form.format(new Date())*/,
            "post created from espresso testing library", "created_at", "updated_at");

    @SuppressWarnings("deprecation")
    public PostsPageTest() {
        // This constructor was deprecated - but we want to support lower API levels.
        super(MainActivity_.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();

        // TODO
        // no need to check network operations here - already done at NetworkRequestsTest
        // so it's a good idea to use https://github.com/square/okhttp/tree/master/mockwebserver here
        // may be with https://github.com/square/dagger/ to inject it in UI

        // As the way to access Instrumentation is changed in the new runner, we need to inject it
        // manually into ActivityInstrumentationTestCase2.
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());

        // Espresso will not launch our activity for us, we must launch it via getActivity().
        mActivity = getActivity();

        // in order to synchronize
        RequestStatusObject rso = RequestStatusObject.getInstance();
        mRequestIR = new RequestStatusIdlingResource(rso);
        registerIdlingResources(mRequestIR);
    }

    @Test
    public void testPostAddClickRemove() throws IOException {

        onView(withId(R.id.action_add))
                .perform(click());

        onView(withId(R.id.button_add))
                .check(matches(withText("add post")));

        onView(withId(R.id.text_title))
                .perform(typeText(sTestPost.title));

        onView(withId(R.id.text_body))
                .perform(typeText(sTestPost.body));

        onView(withId(R.id.button_add))
                .perform(click());

        // TODO write correct matcher
        /*onView(withId(R.id.list_posts))
                .check(matches(withAdaptedData(withItemContent(sTestPost.title))));*/

        onView(Matchers.allOf(ViewMatchers.withId(R.id.text_title),
                ViewMatchers.hasSibling(ViewMatchers.withText(sTestPost.title))))
                .check(matches(withText(sTestPost.title)));

        onView(Matchers.allOf(ViewMatchers.withId(R.id.text_title),
                ViewMatchers.hasSibling(ViewMatchers.withText(sTestPost.title)))).perform(click());
        onView(withId(R.id.post_content))
                .check(matches(withText(sTestPost.body)));

        onView(isRoot()).perform(pressBack());

        onView(Matchers.allOf(ViewMatchers.withId(R.id.text_title),
                ViewMatchers.hasSibling(ViewMatchers.withText(sTestPost.title)))).perform(swipeRight());
        // 3000 delay in EnhancedListView before network "delete post" request
        mRequestIR.lock();

        onView(withText(sTestPost.title))
                .check(doesNotExist());
    }

    @After
    public void tearDown() throws Exception {
        // Make sure that we call the tearDown() method of ActivityInstrumentationTestCase2
        // to clean up and not leak any objects.
        super.tearDown();
    }

    private class RequestStatusIdlingResource implements IdlingResource,
            RequestStatusObject.RequestStatusChange {
        private RequestStatusObject mState;
        private ResourceCallback mCallback;

        public RequestStatusIdlingResource(RequestStatusObject status) {
            mState = checkNotNull(status);
        }

        @Override
        public String getName() {
            return "is request finished";
        }

        @Override
        public boolean isIdleNow() {
            return !mState.getState().equals(RequestStatusObject.IN_PROCESS);
        }

        @Override
        public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
            mCallback = resourceCallback;
            mState.setResourceCallback(this);
        }

        @Override
        public void onChange() {
            mCallback.onTransitionToIdle();
        }


        public void lock() {
            mState.setStarted();
        }
    }
}
