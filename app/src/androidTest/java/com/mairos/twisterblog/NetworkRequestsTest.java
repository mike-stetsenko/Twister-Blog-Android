package com.mairos.twisterblog;

import android.test.suitebuilder.annotation.MediumTest;

import com.mairos.twisterblog.model.Post;
import com.mairos.twisterblog.model.RequestResult;
import com.mairos.twisterblog.network.TwisterBlogHttp;
import com.mairos.twisterblog.network.TwisterBlogService;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import retrofit.RestAdapter;

/**
 * Created by Mike on 11.01.2015.
 */

@RunWith(OrderedRunner.class)
// TODO import junit:4.11
//@FixMethodOrder
@MediumTest
public class NetworkRequestsTest extends TestCase {

    private TwisterBlogHttp mRetrofitService;
    private RestAdapter mRestAdapter;

    private static volatile int sPostId = 0;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        mRestAdapter = new RestAdapter.Builder()
                .setServer(TwisterBlogService.sBaseUrl)
                .build();

        mRetrofitService = mRestAdapter.create(TwisterBlogHttp.class);
    }

    @Test
    // FIXME - anyway, ordering looks like pretty bicycle
    @Order(order=1)
    public void testAddPost() {
        RequestResult res = mRetrofitService.addPost("test post title - NetworkRequestsTest", "test post title - NetworkRequestsTest");
        assertEquals(RequestResult.SUCCESS_MESSAGE, res.message);
    }

    @Test
    @Order(order=2)
    public void testGetAllPosts() {
        Post.List posts = mRetrofitService.getPosts();
        assertNotNull("there are no posts in database", posts);
        sPostId = posts.get(posts.size()-1).id;
    }

    @Test
    @Order(order=3)
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
