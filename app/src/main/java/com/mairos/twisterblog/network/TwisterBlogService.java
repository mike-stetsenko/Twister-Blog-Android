package com.mairos.twisterblog.network;

import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

public class TwisterBlogService extends RetrofitGsonSpiceService {

    private final static String BASE_URL = TwisterBlog.API_URL;

    @Override
    public void onCreate() {
        super.onCreate();
        addRetrofitInterface(TwisterBlog.class);
    }

    @Override
    protected String getServerUrl() {
        return BASE_URL;
    }

}
