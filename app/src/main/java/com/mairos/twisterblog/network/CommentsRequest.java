package com.mairos.twisterblog.network;

import com.mairos.twisterblog.model.Comment;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

public class CommentsRequest extends RetrofitSpiceRequest<Comment.List, TwisterBlog> {

    private int mPostId = 0;

    public CommentsRequest(int postId) {
        super(Comment.List.class, TwisterBlog.class);
        mPostId = postId;
    }

    @Override
    public Comment.List loadDataFromNetwork() {
        return getService().getComments(mPostId);
    }
}
