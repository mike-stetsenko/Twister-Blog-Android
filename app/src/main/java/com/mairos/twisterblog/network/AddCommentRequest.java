package com.mairos.twisterblog.network;

import com.mairos.twisterblog.model.RequestResult;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

public class AddCommentRequest extends RetrofitSpiceRequest<RequestResult, TwisterBlogHttp> {

    private int mPostId;
    private String mBody;

    public AddCommentRequest(int postId, String body) {
        super(RequestResult.class, TwisterBlogHttp.class);
        mPostId = postId;
        mBody = body;
    }

    @Override
    public RequestResult loadDataFromNetwork() {
        return getService().addComment(mPostId, mBody);
    }
}
