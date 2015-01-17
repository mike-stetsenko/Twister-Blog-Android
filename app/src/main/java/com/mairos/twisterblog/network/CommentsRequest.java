package com.mairos.twisterblog.network;

import com.mairos.twisterblog.model.Comment;
import com.mairos.twisterblog.storage.Storage;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

public class CommentsRequest extends RetrofitSpiceRequest<Comment.List, TwisterBlogHttp> {

    private int mPostId = 0;

    public CommentsRequest(int postId) {
        super(Comment.List.class, TwisterBlogHttp.class);
        mPostId = postId;
    }

    @Override
    public Comment.List loadDataFromNetwork() {
        Comment.List comments = getService().getComments(mPostId);

        if (comments != null) {
            Storage.get().deleteCommentsByPost(mPostId);
            Storage.get().saveComments(comments);
        }

        return comments;
    }
}
