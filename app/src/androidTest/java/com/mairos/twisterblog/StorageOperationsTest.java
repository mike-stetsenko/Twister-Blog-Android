package com.mairos.twisterblog;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.MediumTest;

import com.mairos.twisterblog.model.Post;
import com.mairos.twisterblog.storage.Storage;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Created by Mike on 11.01.2015.
 */

@RunWith(AndroidJUnit4.class)
@FixMethodOrder
@MediumTest
public class StorageOperationsTest extends TestCase {

    private Storage mStorage;
    private static final Post examplePost = new Post(1, "sample body",
            "sample title", "date", "update");

    @Before
    public void setUp() throws Exception {
        super.setUp();
        mStorage = Storage.get();
    }

    @Test
    public void testSavePost() {
        long res = mStorage.savePost(examplePost);
        assertTrue("post was not added to table", res >= 0);
    }

    @Test
    public void testGetAllPosts() {
        List<Post> posts = mStorage.getPosts();
        assertTrue("posts were not extracted from table", posts.size() > 0);
    }

    @Test
    public void testRemovePosts() {
        int res = mStorage.deleteAllPosts();
        assertTrue("post was not removed from table", res > 0);
    }

    @After
    public void tearDown() throws Exception {
        // Make sure that we call the tearDown() method of ActivityInstrumentationTestCase2
        // to clean up and not leak any objects.
        super.tearDown();
    }
}
