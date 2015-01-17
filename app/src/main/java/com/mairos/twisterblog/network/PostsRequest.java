package com.mairos.twisterblog.network;

import com.mairos.twisterblog.model.Post;
import com.mairos.twisterblog.storage.Storage;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

public class PostsRequest extends RetrofitSpiceRequest<Post.List, TwisterBlogHttp> {

    public PostsRequest() {
        super(Post.List.class, TwisterBlogHttp.class);
    }

    @Override
    public Post.List loadDataFromNetwork() {
        Post.List posts = getService().getPosts();

        if (posts != null) {
            Storage.get().deleteAllPosts();
            Storage.get().savePosts(posts);
        }

        return posts;
    }
}
