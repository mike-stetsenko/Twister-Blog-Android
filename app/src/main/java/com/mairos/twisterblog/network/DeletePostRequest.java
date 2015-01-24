package com.mairos.twisterblog.network;

import com.mairos.twisterblog.model.RequestResult;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

public class DeletePostRequest extends RetrofitSpiceRequest<RequestResult, TwisterBlogHttp> {

    private int mPostId;

    public DeletePostRequest(int postId) {
        super(RequestResult.class, TwisterBlogHttp.class);
        mPostId = postId;
    }

    @Override
    public RequestResult loadDataFromNetwork() {
        RequestResult rr = getService().deletePost(mPostId);
        return rr;
    }
}
