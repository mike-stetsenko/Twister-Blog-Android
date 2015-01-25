package com.mairos.twisterblog.network;

import com.mairos.twisterblog.Constants;
import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

public class TwisterBlogService extends RetrofitGsonSpiceService {

    public final static String sBaseUrl = Constants.API_URL;

    @Override
    public void onCreate() {
        super.onCreate();
        addRetrofitInterface(TwisterBlogHttp.class);
    }

    @Override
    protected String getServerUrl() {
        return sBaseUrl;
    }

}
