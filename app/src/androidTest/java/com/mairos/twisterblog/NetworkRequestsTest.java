package com.mairos.twisterblog;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;

import com.mairos.twisterblog.model.Post;
import com.mairos.twisterblog.model.RequestResult;
import com.mairos.twisterblog.network.TwisterBlogHttp;
import com.mairos.twisterblog.network.TwisterBlogService;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;

import retrofit.RestAdapter;

/**
 * Created by Mike on 11.01.2015.
 */

@RunWith(AndroidJUnit4.class)
@FixMethodOrder
@MediumTest
public class NetworkRequestsTest extends TestCase {

    private TwisterBlogHttp mRetrofitService;
    private RestAdapter mRestAdapter;

    private static volatile int sPostId = 0;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        mRestAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.API_URL)
                .build();

        mRetrofitService = mRestAdapter.create(TwisterBlogHttp.class);
    }

    @Test
    public void testAddPost() {
        RequestResult res = mRetrofitService.addPost("test post title - NetworkRequestsTest", "test post title - NetworkRequestsTest");
        assertEquals(RequestResult.SUCCESS_MESSAGE, res.message);
    }

    @Test
    public void testGetAllPosts() {
        Post.List posts = mRetrofitService.getPosts();
        assertNotNull("there are no posts in database", posts);
        sPostId = posts.get(posts.size()-1).id;
    }

    @Test
    public void testRemovePost() {
        RequestResult res = mRetrofitService.deletePost(sPostId);
        assertEquals(RequestResult.SUCCESS_MESSAGE, res.message);
    }

    @After
    public void tearDown() throws Exception {
        // Make sure that we call the tearDown() method of ActivityInstrumentationTestCase2
        // to clean up and not leak any objects.
        super.tearDown();
    }
}
