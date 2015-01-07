package com.mairos.twisterblog.network;

import com.mairos.twisterblog.model.Post;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

public class PostsRequest extends RetrofitSpiceRequest<Post.List, TwisterBlog> {

    public PostsRequest() {
        super(Post.List.class, TwisterBlog.class);
    }

    @Override
    public Post.List loadDataFromNetwork() {
        return getService().getPosts();
    }
}
