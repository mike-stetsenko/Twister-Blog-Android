package com.mairos.twisterblog.network;

import com.mairos.twisterblog.model.RequestResult;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

public class AddPostRequest extends RetrofitSpiceRequest<RequestResult, TwisterBlogHttp> {

    private String mTitle;
    private String mBody;

    public AddPostRequest(String title, String body) {
        super(RequestResult.class, TwisterBlogHttp.class);
        mTitle = title;
        mBody = body;
    }

    @Override
    public RequestResult loadDataFromNetwork() {
        return getService().addPost(mTitle, mBody);
    }
}
